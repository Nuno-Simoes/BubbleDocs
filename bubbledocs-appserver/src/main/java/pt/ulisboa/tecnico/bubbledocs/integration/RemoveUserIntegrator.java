package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.dto.UserDto;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUserService;
import pt.ulisboa.tecnico.bubbledocs.service.GetUserInfoService;
import pt.ulisboa.tecnico.bubbledocs.service.RemoveUserService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RemoveUserIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private String username;
	private UserDto dto;

	public RemoveUserIntegrator(String userToken, String username) {
		this.userToken = userToken;
		this.username = username;
		GetUserInfoService info = new GetUserInfoService(username);
		info.execute();
		this.dto = info.getResult();
	}
	
	@Override
	protected void dispatch() throws UnavailableServiceException {
		
		RemoveUserService localService = new RemoveUserService(userToken, username);
		IDRemoteServices remoteService = new IDRemoteServices();
		
		try {
			localService.execute();
			remoteService.removeUser(username);
		} catch (RemoteInvocationException rie) {
			CreateUserService compensationService =
					new CreateUserService(userToken, dto.getUsername(), 
							dto.getEmail(), dto.getName());
			compensationService.execute();
			throw new UnavailableServiceException();
		}
	}

}
