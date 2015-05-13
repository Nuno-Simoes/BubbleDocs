package id.ws.impl;

public class User {

	private String userId;
	private String password;
	private String emailAddress;
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	static Integer count = 10;

	public User(String userId, String password, String emailAddress) {
		this.userId = userId;
		this.password = password;
		this.emailAddress = emailAddress;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	
	public static String generateRandomString() {
		StringBuffer buffer = new StringBuffer();
		int charactersLength = ALPHA_NUMERIC_STRING.length();

		for (int i = 0; i < count; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(ALPHA_NUMERIC_STRING.charAt((int) index));
		}
		return buffer.toString();
	}
	
	public void setPassword() {
		this.password = generateRandomString();
	}

}