package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordService extends PortalService {

	private String userToken;	
	
	public RenewPasswordService(String userToken) {
		this.userToken= userToken;
	}
	
	@Override
	protected void dispatch() throws LoginBubbleDocsException, UnavailableServiceException, RemoteInvocationException {
		User u= getUser(userToken);
		try {
			IDRemoteServices remote= new IDRemoteServices();
			remote.renewPassword(u.getUsername());
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}
		catch (LoginBubbleDocsException lbde){
			throw new UnavailableServiceException();
		}
		}

}
