package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Content extends Content_Base {
    
    public Content() {
        super();
    }
    
    //ABSTRACT METHOD
    public double getResult() {
    	return Double.NaN;
    }
    
    //ABSTRACT METHOD
    public Element exportToXML() {
    	return null;
    }
    
    //ABSTRACT METHOD
    public void importFromXML(Element e) {}
    
    //ABSTRACT METHOD
    public void importFromXML(Element e, Cell c) {}
    
    //ABSTRACT METHOD
    public void delete() {}
    
}
