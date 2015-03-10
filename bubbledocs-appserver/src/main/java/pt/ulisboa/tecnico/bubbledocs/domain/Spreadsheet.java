package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.Date;


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
}
