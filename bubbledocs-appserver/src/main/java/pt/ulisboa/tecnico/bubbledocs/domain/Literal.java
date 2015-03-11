package pt.ulisboa.tecnico.bubbledocs.domain;

public class Literal extends Literal_Base {
    
	public Literal() {
		super();
	}

	public Literal(double value) {
		super();
		this.init(value);
	}

	protected void init(double value) {
		this.setValue(value);
	}
}
