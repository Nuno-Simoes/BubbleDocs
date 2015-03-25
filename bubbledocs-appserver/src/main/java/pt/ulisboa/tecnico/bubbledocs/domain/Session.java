package pt.ulisboa.tecnico.bubbledocs.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.WrongPasswordException;


public class Session extends Session_Base {
	
	private static Session instance = null;
	private List<User> activeSessions = new ArrayList<User>();
	private long oneHours = 3600000;
	
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
		int currentTime = (int) (System.currentTimeMillis()/oneHours);
		user.setSessionTime(currentTime);
	}
	
	
	private void removeOldSessions() {
		int currentTime = (int) (System.currentTimeMillis()/oneHours);
		
		for (User u : activeSessions) {
			if ((currentTime - u.getSessionTime()) >= 2) {
				u.setToken(null);
				u.setSessionTime(0);
				this.activeSessions.remove(u);
			} else {
				break;
			}
		}
		
	}
	
	public void login(String username, String password) 
		throws InvalidPermissionException, UserDoesNotExistException {
		
		Portal p = Portal.getInstance();
		User u = p.findUser(username);
		
		if (u.getUsername().equals(username) 
				&& u.getPassword().equals(password)) {
			
			if(this.isAlreadyLogged(username)) {
				User loggedUser = this.findUserByUsername(username);
				loggedUser.setToken(null);
				loggedUser.setToken(null);
				this.activeSessions.remove(loggedUser);
			}
			
			this.createToken(u);
			this.setSessionTime(u);
			this.removeOldSessions();
			activeSessions.add(u);
		} else {
			throw new WrongPasswordException(username);
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
	
	public User findUserByUsername(String username) throws UserNotLoggedException {
		for (User u : activeSessions) {
    		if (u.getUsername().equals(username)) {
    			return u;
    		}
    	}
    	throw new UserNotLoggedException(username);
	}
	
	private boolean isAlreadyLogged(String username) {
		for (User u : activeSessions) {
    		if (u.getUsername().equals(username)) {
    			return true;
    		}
    	}
    	return false;
    }
	
	public boolean isTokenActive(String token) {
		for (User u : activeSessions) {
    		if (u.getToken().equals(token)) {
    			return true;
    		}
    	}
    	return false;
    }
	
	
}
