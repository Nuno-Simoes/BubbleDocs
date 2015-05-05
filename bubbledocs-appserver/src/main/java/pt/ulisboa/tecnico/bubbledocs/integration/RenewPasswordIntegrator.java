package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.RenewPasswordService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {

	private String userToken;

	public RenewPasswordIntegrator(String userToken) {
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws UnavailableServiceException {

		IDRemoteServices remoteService = new IDRemoteServices();
		RenewPasswordService localService = new RenewPasswordService(userToken);

		try {
			remoteService.renewPassword(userToken);
			localService.execute();
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}

	}

}