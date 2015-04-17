package id.ws.impl;

public class User {
	
	String userId;
	String password;
	String emailAddress;
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	static Integer count = 10;
	
	public User (String userId, String password, String emailAddress){
		this.userId = userId;
		this.password = password;
		this.emailAddress = emailAddress;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getEmailAddress() {
		return this.emailAddress;
	}
	
	public static String password() {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	public void setPassword() {
		this.password = password();
	}
	
}







