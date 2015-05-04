package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbledocsException;

public abstract class BubbleDocsIntegrator {

	public final void execute() throws BubbledocsException {
		dispatch();
	}
	
	protected abstract void dispatch() throws BubbledocsException;
}
