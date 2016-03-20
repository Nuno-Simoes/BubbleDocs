package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.Random;

import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;

public class Session extends Session_Base {
	
	private static Session instance = null;
	private long oneHour = 3600000;
	
	protected Session() {
	}
	
	public static Session getInstance() {
		if(instance == null) {
			instance = new Session();
		}
		return instance;
	}
	
	public void login(String username, String password, boolean status)
			throws UnavailableServiceException  {
		Portal p = Portal.getInstance();
		User u = p.findUser(username);

		if(!status) {
			u.setPassword(password);
		}
				
		if ((u.getPassword())==null) {
			throw new UnavailableServiceException();
		}
		
		if ((u.getPassword()).equals(password)) {
			this.createToken(u);
			this.setSessionTime(u);
			this.addUsers(u);
			this.removeOldUsers();
		} else {
			throw new UnavailableServiceException();
		}
	}
    
    public void logout(User u) {
    	u.setSessionTime(0);
    	u.setToken(null);
    	this.getUsersSet().remove(u);
    }
    
    private void removeOldUsers() {
    	for (User loggedUser : this.getUsersSet()) {
    		if(timeExceeded(loggedUser)){
    			this.getUsersSet().remove(loggedUser);
    		}
    	}
    }

    private void setSessionTime(User u) {
		float currentTime = (float) (System.currentTimeMillis()/oneHour);
		u.setSessionTime(currentTime);
	}
    
    private boolean timeExceeded(User u) {
    	float currentTime = (float) (System.currentTimeMillis()/oneHour);
    	float lastLoginTime = u.getSessionTime();
    	if(currentTime-lastLoginTime > 2) {
    		return true;
    	}
    	return false;
    }
    	
    private void createToken(User u) {
    	Random rand = new Random();
    	int low = 0;
    	int high = 9;
    	int r = rand.nextInt(high-low) + low;
    	u.setToken(u.getUsername().concat(Integer.toString(r)));
    }
    
    public boolean isInSession(User u) {
    	for (User loggedUser : this.getUsersSet()) {
    		if(loggedUser.equals(u)){
    			return true;
    		}
    	}
    	return false;
    }
    
    public User getLoggedUser(String token) throws LoginBubbleDocsException {
    	for (User loggedUser : this.getUsersSet()) {
    		if(loggedUser.getToken().equals(token)){
    			return loggedUser;
    		}
    	}
    	throw new LoginBubbleDocsException();
    }
    
    public boolean isValidSession(String token) {
    	for (User loggedUser : this.getUsersSet()) {
    		if(loggedUser.getToken().equals(token) && !timeExceeded(loggedUser)) {
    			return true;
    		}
    	}
    	return false;
    }
 
}
