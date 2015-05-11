package id.ws.impl;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class User {

	String userId;
	String password;
	String emailAddress;
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	static Integer count = 10;

	private SecretKey secretKey;
	private IvParameterSpec iv;

	public User(String userId, String password, String emailAddress) {
		this.userId = userId;
		this.password = password;
		this.emailAddress = emailAddress;
		this.secretKey = generateKey();
		this.iv = generateIv();
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

	public SecretKey generateKey() {
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyGen.init(128);
		SecretKey secretKey = keyGen.generateKey();
		return secretKey;
	}

	public IvParameterSpec generateIv() {
		final int AES_KEYLENGTH = 128;
		byte[] iv = new byte[AES_KEYLENGTH/8];
		IvParameterSpec ivParam = new IvParameterSpec(iv);
		return ivParam;
	}

	public SecretKey getSecretKey() {
		return this.secretKey;
	}

	public IvParameterSpec getIv() {
		return this.iv;
	}

}