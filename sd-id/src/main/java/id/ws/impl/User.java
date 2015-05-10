package id.ws.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class User {
	
	String userId;
	String password;
	String emailAddress;
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	static Integer count = 10;
	
	private SecretKey secretKey;
	private SecureRandom iv;
	private byte[] cipherPwBytes;
	private byte[] decipherPwBytes;
	
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
		System.out.println("Nova password: " + builder.toString());
		
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
	
	public SecureRandom generateIv() {
		final int AES_KEYLENGTH = 128;
		byte[] iv = new byte[AES_KEYLENGTH/8];
		SecureRandom ivParam = new SecureRandom(iv);
		return ivParam;
	}
	
	public SecretKey getKey() {
		return this.secretKey;
	}
	
	public SecureRandom getIv() {
		return this.iv;
	}
	
	public byte[] cipher(String password) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		byte[] passwordBytes = password.getBytes();
		cipherPwBytes = cipher.doFinal(passwordBytes);
		return cipherPwBytes;
	}
	
	public byte[] decipher(SecretKey secretKey, byte[] cipherPwBytes) throws Exception {
		Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		decipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		decipherPwBytes = decipher.doFinal(cipherPwBytes);
		return decipherPwBytes;
	}
	
}