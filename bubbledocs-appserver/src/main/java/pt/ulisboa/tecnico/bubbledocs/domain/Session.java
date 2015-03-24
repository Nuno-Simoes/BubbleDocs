package pt.ulisboa.tecnico.bubbledocs.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;


public class Session extends Session_Base {
	
	private static Session instance = null;
	private List<User> activeSessions = new ArrayList<User>();
	private long twoHours = 7200000;
	
	protected Session() {
	}
	
	public static Session getInstance() {
		if(instance == null) {
			instance = new Session();
		}
		return instance;
	}

	private void createToken(User user) {
    	Random rand = new Random();
    	int low = 0;
    	int high = 9;
    	int r = rand.nextInt(high-low) + low;
	   
    	user.setToken(user.getUsername().concat(Integer.toString(r)));
    }
	
	private void setSessionTime(User user) {
		long currentTime = System.currentTimeMillis();
		user.setSessionTime(currentTime);
	}
	
	
	private void removeOldSessions() {
		long currentTime = System.currentTimeMillis();
		
		for (User u : activeSessions) {
			if ((currentTime - u.getSessionTime()) >= twoHours) {
				u.setToken(null);
				u.setSessionTime(0);
				activeSessions.remove(u);
			} else {
				break;
			}
		}
		
	}
	
	public void login(String username, String password) 
		throws InvalidPermissionException {
		
		Portal p = Portal.getInstance();
		User u = p.findUser(username);
		
		System.out.printf("username:%s\n", username);
		if (u.getUsername().equals(username) 
				&& u.getPassword().equals(password)) {
			this.createToken(u);
			this.setSessionTime(u);
			this.removeOldSessions();
			activeSessions.add(u);
		} else {
			throw new InvalidPermissionException(username);
		}
	}
	
	
	public void removeUser(String token) throws UserDoesNotExistException {
		for (User u : activeSessions) {
			if (u.getToken().equals(token)) {
				u.setToken(null);
				u.setSessionTime(0);
				activeSessions.remove(u);
			}
		}
	}
	
	public User findUser(String token) throws UserNotLoggedException {
    	for (User u : activeSessions) {
    		if (u.getToken().equals(token)) {
    			return u;
    		}
    	}
    	throw new UserNotLoggedException(token);
    }
}
