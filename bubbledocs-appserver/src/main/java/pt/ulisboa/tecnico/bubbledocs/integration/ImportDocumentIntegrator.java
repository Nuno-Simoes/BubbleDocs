package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSessionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.ulisboa.tecnico.bubbledocs.service.ImportDocumentService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {
	
	private String docId;
	private String userToken;
	private Spreadsheet result;
	
	public ImportDocumentIntegrator (String userToken, String docId) {
		this.docId = docId;
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws UnavailableServiceException, InvalidSessionException {
		byte[] newFile = null;
		GetUsername4TokenService service = new GetUsername4TokenService(userToken);
		service.execute();
		String username = service.getResult();
		
		if(Session.getInstance().isValidSession(userToken)) {
			try {
				StoreRemoteServices remoteService = StoreRemoteServices.getInstance();
				newFile = remoteService.loadDocument(username, docId.toString());
				ImportDocumentService localService = 
						new ImportDocumentService(newFile, username);
				localService.execute();
				result = localService.getResult();
			} catch (RemoteInvocationException rie) {
				throw new UnavailableServiceException();
			}
		} else {
			throw new InvalidSessionException(userToken);
		}
	}
	
	public Spreadsheet getResult() {
		return this.result;
	}

}
