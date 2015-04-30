package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.Random;

import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

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
	
	public void login(String username, String password)
			throws UserDoesNotExistException, UnavailableServiceException  {
		Portal p = Portal.getInstance();
		User u = p.findUser(username);
		String userPassword = u.getPassword();

		if (userPassword==null) {
			throw new UnavailableServiceException();
		} else if(userPassword.equals(password)) {
			this.createToken(u);
			this.setSessionTime(u);
			this.addUsers(u);
			this.removeOldUsers();
		} else {
			throw new  UnavailableServiceException();
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
}