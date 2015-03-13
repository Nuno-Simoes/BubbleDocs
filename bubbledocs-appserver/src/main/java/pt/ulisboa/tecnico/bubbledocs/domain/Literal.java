package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Literal extends Literal_Base {
    
	public Literal() {
		super();
	}

	public Literal(int literal) {
		super();
		this.init(literal);
	}

	protected void init(int literal) {
		this.setLiteral(literal);
	}
	
	@Override
	public double getResult() {
		return this.getLiteral();
	}
	
	/*public void importFromXML(Element literalElement) {
		try {
			setLiteral(literalElement.getAttribute("value").getIntValue());
		} catch (DataConversionException e) { 
			throw new ImportDocumentException();
		}
	    }*/

	    public Element exportToXML() {
		Element element = new Element("literal");
		element.setAttribute("value", Double.toString(getLiteral()));		
		return element;
	    }
}
