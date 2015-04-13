package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;

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

	public Element exportToXML() {
		Element element = new Element("literal");
		
		if (this.getLiteral() == -1 || Double.isNaN(this.getLiteral())) {
			element.setAttribute("value", "#VALUE");		
		} else {
			element.setAttribute("value", Double.toString(this.getLiteral()));
		}
		
		return element;
	}
	
	public void importFromXML (Element element) {
		try {
			if (element.getAttribute("value").getValue().equals("#VALUE")) {
				this.setLiteral(-1);
			} else {
				this.setLiteral((int)element.getAttribute("value").getDoubleValue());
			}
		} catch (DataConversionException dce) {
			throw new ImportDocumentException();
		}
    }
	
	public void delete() {
		this.setCell(null);
	}
	
}
