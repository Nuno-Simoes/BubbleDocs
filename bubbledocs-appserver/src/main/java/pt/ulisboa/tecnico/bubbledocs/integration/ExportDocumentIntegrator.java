package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbledocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.ExportDocumentService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private int docId;
	private String username;
	private String docName;
	private byte[] document;

	public ExportDocumentIntegrator(String userToken, int docId, String username, String docName){
		this.userToken = userToken;
		this.docId = docId;
		this.username = username;
		this.docName = docName;
	}

	@Override
	protected void dispatch() throws BubbledocsException, UnavailableServiceException {
		ExportDocumentService localService = new ExportDocumentService(userToken, docId);
		StoreRemoteServices remoteService = new StoreRemoteServices();

		try {
			localService.execute();
			this.document = localService.getResult();
			remoteService.storeDocument(username, docName, document);
		} catch (RemoteInvocationException rie){
			throw new UnavailableServiceException();
		}
	}
	
	public final byte[] getResult() {
        return this.document;
    }
}