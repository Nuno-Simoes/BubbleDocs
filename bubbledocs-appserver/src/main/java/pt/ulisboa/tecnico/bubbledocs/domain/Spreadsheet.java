package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.Date;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
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

    public Cell splitCellId(String cellId){
		String string = cellId;
		String[] parts = string.split(";");
		int part1 = Integer.parseInt(parts[0]); 
		int part2 = Integer.parseInt(parts[1]);
		
		return getCell(part1, part2);
    }
    
    
    public Cell splitCellReference(String reference){
    	String strings = reference;
		String[] part = strings.split(";");
		int part3, part4;
		try {
			part3 = Integer.parseInt(part[0]); 
			part4 = Integer.parseInt(part[1]);
		} catch (NumberFormatException nfe) {
			throw new InvalidContentException();
		}
		return getCell(part3, part4);
    }
    
    public BinaryFunction parseBinaryFunction(String binaryFunction) 
    		throws InvalidContentException  {
    	String[] initialSplit = binaryFunction.split("\\(");
    	String function = initialSplit[0];
    	
    	String[] secondSplit = initialSplit[1].split(",");
    	
    	if (secondSplit.length!=2) {
    		throw new InvalidContentException();
    	}
    	
    	String firstPart = secondSplit[0];
    	
    	String[] thirdSplit;
    	thirdSplit = secondSplit[1].split("\\)");
    	
    	String secondPart = thirdSplit[0];
    	
    	String[] firstPartTest = firstPart.split(";");
    	String[] secondPartTest = secondPart.split(";");
    	
    	Argument firstArgument;
    	Argument secondArgument;
    	
    	if(firstPartTest.length==1) {
    		try {
    			firstArgument = new Literal(Integer.parseInt(firstPart));
    		} catch (NumberFormatException nfe) {
    			throw new InvalidContentException();
    		}
    	} else {
    		firstArgument = new Reference(this.splitCellReference(firstPart));
    	}
    	
    	if(secondPartTest.length==1) {
    		try {
    		secondArgument = new Literal(Integer.parseInt(secondPart));
    		} catch (NumberFormatException nfe) {
			throw new InvalidContentException();
    		}
    	} else {
    		secondArgument = new Reference(this.splitCellReference(secondPart));
    	}
    	
    	switch(function) {
    		case "ADD":
    			BinaryFunction add = new Add(firstArgument, secondArgument);
    			return add;
    		case "SUB":
    			BinaryFunction sub = new Sub(firstArgument, secondArgument);
    			return sub;
    		case "MULT":
    			BinaryFunction mult = new Mult(firstArgument, secondArgument);
    			return mult;
    		case "DIV":
    			BinaryFunction div = new Div(firstArgument, secondArgument);
    			return div;
    		default: 
    			throw new InvalidContentException();
    	} 	
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
		
		return element;
	}
	
	public void importFromXML (Element element) {
		this.setName(element.getAttributeValue("name"));
		//this.setOwner(element.getAttributeValue("owner"));
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
		
		Permission perm = new Permission(true, true);
		Portal p = Portal.getInstance();
		User u = p.findUser(this.getOwner());
		perm.setUser(u);
		perm.setSpreadsheet(this);
		this.addPermissions(perm);
		u.addPermissions(perm);
		
	}
	
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
			   p.getUser().removePermissions(p);
			   p.setUser(null);
		}
		
		for (Cell c : this.getCellsSet()) {
			this.removeCells(c);
			c.delete();
		}
    }
}
