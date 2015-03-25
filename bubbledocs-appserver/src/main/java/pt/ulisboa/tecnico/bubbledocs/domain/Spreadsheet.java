package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.Date;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
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
	}

	public Element exportToXML() {
		Element element = new Element("spreadsheet");

		element.setAttribute("name", this.getName());
		element.setAttribute("owner", this.getOwner());
		element.setAttribute("date", this.getDate());
		element.setAttribute("lines", Integer.toString(getLines()));
		element.setAttribute("columns", Integer.toString(getColumns()));

		Element cellsElement = new Element("cell");
		element.addContent(cellsElement);

		for (Cell c : getCellsSet()) {
			cellsElement.addContent(c.exportToXML());
		}
		
		for (Permission p : this.getPermissionsSet()) {
			this.removePermissions(p);
		}
		
		return element;
	}
	
	public void importFromXML (Element element) {
		this.setName(element.getAttributeValue("name"));
		this.setOwner(element.getAttributeValue("owner"));
		Date date = new Date();
		String newDate = date.toString();
		this.setDate(newDate);
		
		try {
			this.setColumns(element.getAttribute("columns").getIntValue());
			this.setLines(element.getAttribute("lines").getIntValue());
		} catch (DataConversionException dce) {
			throw new ImportDocumentException();
		}
		
		Element cells = element.getChild("cell");
		for (Element c : cells.getChildren("cell")) {
			Cell newCell = new Cell();
			
			try {
				newCell.setLine(c.getAttribute("line").getIntValue());
	    		newCell.setColumn(c.getAttribute("column").getIntValue());
	    	} catch (DataConversionException dce) {
	    		throw new ImportDocumentException();
	    	}
			this.addCells(newCell);
		}
		
		for (Cell c : this.getCellsSet()) {
			for (Element e : cells.getChildren("cell")) {
				try {
					if (c.getLine()==e.getAttribute("line").getIntValue() 
							&& c.getColumn()==e.getAttribute("column").getIntValue()) {
						c.importFromXML(e);
					}
				} catch (DataConversionException dce) {
					throw new ImportDocumentException();
				}
			}
		}
	}
	
/*	public void importFromXML(Element spreadsheetElement) {
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
		}*/
		
		/*Element permissions = spreadsheetElement.getChild("permission");

		for (Element p : permissions.getChildren("permission")) {
			Permission permissionp = new Permission();
			permissionp.importFromXML(p);
			addPermissions(permissionp);
		}*/
	//}
	
	public Cell getCell(int line, int column) throws OutOfBoundsException {
		for (Cell c : this.getCellsSet()) {
			if (c.getLine()==line && c.getColumn()==column) {
				return c;
			}
		}
		if (this.getLines() >= line && this.getColumns() >= column) {
			Cell c = new Cell(line, column);
			this.addCells(c);
			return c;
		}
		throw new OutOfBoundsException("line " + line + " and column " + column);
	}
	
	public void setContent (int line, int column, Content content) 
			throws OutOfBoundsException, InvalidPermissionException {
		Cell c = getCell(line, column);
		if (c.getIsProtected()) {
			throw new InvalidPermissionException();
		} else {
			c.setContent(content);
		}
	}

	public void delete() {
    	
		for (Permission p : this.getPermissionsSet()) {
			   this.removePermissions(p);
			   p.setSpreadsheet(null);
			   p.setUser(null);
		}
		
		for (Cell c : this.getCellsSet()) {
			this.removeCells(c);
			c.delete();
		}
    }
}
