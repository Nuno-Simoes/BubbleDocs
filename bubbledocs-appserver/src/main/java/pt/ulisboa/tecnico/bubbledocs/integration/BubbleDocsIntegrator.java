package pt.ulisboa.tecnico.bubbledocs.integration;

public abstract class BubbleDocsIntegrator {

	public final void execute() throws Exception {
		dispatch();
	}
	
	protected abstract void dispatch() throws Exception;
}
