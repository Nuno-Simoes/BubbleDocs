package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.Date;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;

public class Spreadsheet extends Spreadsheet_Base {

	public Spreadsheet() {
		super();
	}

	public Spreadsheet(String name, int lines, int columns) {
		super();
		this.init(name, lines, columns);
	}

	protected void init(String name, int lines, int columns) {
		this.setName(name);

		Date date = new Date();
		String dateText = date.toString(); 
		this.setDate(dateText);

		this.setLines(lines);
		this.setColumns(columns);
		
		for (int i = 1; i <= lines; i++) {
			for (int j = 1; j <= columns; j++) {
				this.addCells(new Cell(i, j));
			}
		}
		
		this.setOwner("Super User");
		
	}

	public Element exportToXML() {
		Element element = new Element("spreadsheet");

		element.setAttribute("name", getName());
		element.setAttribute("owner", getOwner());
		element.setAttribute("date", getDate());
		element.setAttribute("lines", Integer.toString(getLines()));
		element.setAttribute("columns", Integer.toString(getColumns()));
		element.setAttribute("id", Integer.toString(getId()));

		Element cellsElement = new Element("cell");
		element.addContent(cellsElement);

		for (Cell c : getCellsSet()) {
			cellsElement.addContent(c.exportToXML());
		}
		
		/*Element permissionElement = new Element("permission");
		element.addContent(permissionElement);

		for (Permission p : getPermissionsSet()) {
			permissionElement.addContent(p.exportToXML());
		}*/
		

		return element;
	}

	public void importFromXML(Element spreadsheetElement) {
		Element cells = spreadsheetElement.getChild("cell");
		setName(spreadsheetElement.getAttribute("name").getValue());
		setOwner(spreadsheetElement.getAttribute("owner").getValue());
		setDate(spreadsheetElement.getAttribute("date").getValue());
		try {
			setColumns(spreadsheetElement.getAttribute("columns").getIntValue());
			setLines(spreadsheetElement.getAttribute("lines").getIntValue());
			setId(spreadsheetElement.getAttribute("id").getIntValue());
    	} catch (DataConversionException e) { 
    		throw new ImportDocumentException();
    	}
		
		for (Element c : cells.getChildren("cell")) {
			Cell cellp = new Cell();
			cellp.importFromXML(c);
			addCells(cellp);
		}
		
		/*Element permissions = spreadsheetElement.getChild("permission");

		for (Element p : permissions.getChildren("permission")) {
			Permission permissionp = new Permission();
			permissionp.importFromXML(p);
			addPermissions(permissionp);
		}*/
	}
	
	public Cell getCell(int line, int column) throws OutOfBoundsException {
		for (Cell c : this.getCellsSet()) {
			if (c.getLine()==line && c.getColumn()==column) {
				return c;
			}
		}
		
		throw new OutOfBoundsException();
	}
}
