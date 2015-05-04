package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.dto.UserDto;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class GetUserInfoService extends BubbleDocsService {
	
	private UserDto dto;
	private String username;
	
	public GetUserInfoService(String username) {
		this.username = username;
	}

	@Override
	protected void dispatch() throws LoginBubbleDocsException {
		Portal p = Portal.getInstance();
		User u = p.findUser(username);
		String name = u.getName();
		int id = u.getId();
		String token = u.getToken();
		float time = u.getSessionTime();
		String email = u.getEmail();
		dto = new UserDto(username, name, id, token, time, email);
	}
	
	public UserDto getResult() {
		return this.dto;
	}
}
