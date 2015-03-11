package pt.ulisboa.tecnico.bubbledocs.domain;

/*import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;
*/
public class Permission extends Permission_Base {
    
    public Permission() {
        super();
    }
    
    public Permission (boolean read, boolean write) {
    	super();
    	init(read, write);
    }
    
    protected void init (boolean read, boolean write) {
    	this.setRead(read);
    	this.setWrite(write);
    }
    
   /* public void importFromXML(Element permissionElement) {
    	try {
    		setWrite(permissionElement.getAttribute("write").getBooleanValue());
    		setRead(permissionElement.getAttribute("read").getBooleanValue());
    	} catch (DataConversionException e) { 
    		throw new ImportDocumentException();
    	}
    }

    public Element exportToXML() {
    	Element element = new Element("permission");
    	element.setAttribute("read", Boolean.toString(getRead()));
    	element.setAttribute("write", Boolean.toString(getWrite()));

    	return element;
    }*/
    
}
