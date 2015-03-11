package pt.ulisboa.tecnico.bubbledocs.domain;

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
}
