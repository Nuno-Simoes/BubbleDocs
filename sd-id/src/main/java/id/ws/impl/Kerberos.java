package id.ws.impl;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.jdom2.*;
import org.jdom2.output.XMLOutputter;

public class Kerberos {

	private static Kerberos instance = null;
	private static String xmlStringMessage;
	private static String xmlStringTicket;
	private static String xmlCombinedMessage;

	public static Kerberos getInstance() {
		if (instance == null) {
			instance = new Kerberos();
		}
		return instance;
	}

	public SecretKeySpec generatePasswordKey(String password)
			throws NoSuchAlgorithmException {
		MessageDigest sha = null;
		sha = MessageDigest.getInstance("SHA-1");
		byte[] key = sha.digest(password.getBytes());
		key = Arrays.copyOf(key, 16);
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		return secretKey;
	}

	public SecretKeySpec generateSessionKey() {
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyGen.init(128);
		SecretKeySpec secretKey = (SecretKeySpec) keyGen.generateKey();
		return secretKey;
	}

	public byte[] cipher(SecretKeySpec key, String forCipher)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		IvParameterSpec iv = new IvParameterSpec("GUFHVNSMIORLDMTP".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] reserved2 = forCipher.getBytes();
		byte[] cipherPwBytes = cipher.doFinal(reserved2);
		return cipherPwBytes;
	}

	public String decipher(SecretKeySpec key, byte[] forDecipher)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		IvParameterSpec iv = new IvParameterSpec("GUFHVNSMIORLDMTP".getBytes());
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] decipherPwBytes = cipher.doFinal(forDecipher);
		String decipherPassword = new String(decipherPwBytes);
		return decipherPassword;
	}

	public byte[] generateMessage(SecretKeySpec sessionKey, String n,
			SecretKeySpec clientKey) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

		// root element
		Element root = new Element("root");
		Document doc = new Document();
		doc.setRootElement(root);
		root.setAttribute(new Attribute("sessionKey", printBase64Binary(sessionKey.getEncoded())));
		root.setAttribute(new Attribute("n", n));
		xmlStringMessage = new XMLOutputter().outputString(doc);
		byte[] message = cipher(clientKey, xmlStringMessage);
		return message;
	}

	public byte[] generateTicket(String userId, String serverId,
			SecretKeySpec sessionKey, SecretKeySpec serverKey)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		
		Date current = new Date();
		long miliseconds_ten_minutes = 600000;
		long expirationDate = current.getTime() + miliseconds_ten_minutes;
		Date expire = new Date(expirationDate);

		Element root = new Element("root");
		Document doc = new Document();
		doc.setRootElement(root);
		root.setAttribute(new Attribute("userId", userId));
		root.setAttribute(new Attribute("serverId", serverId));
		root.setAttribute(new Attribute("timestamp1", current.toString()));
		root.setAttribute(new Attribute("expiration", expire.toString()));
		root.setAttribute(new Attribute("sessionKey", Base64.getEncoder()
				.encodeToString(sessionKey.getEncoded())));
		xmlStringTicket = new XMLOutputter().outputString(doc);
		byte[] ticket = cipher(serverKey, xmlStringTicket);
		return ticket;

	}

	public byte[] combineMessage(byte[] ticket, byte[] message)
			throws IOException {
		Element root = new Element("root");
		Document doc = new Document();
		doc.setRootElement(root);
		root.setAttribute(new Attribute("part1Message", printBase64Binary(message)));
		root.setAttribute(new Attribute("part2Message", printBase64Binary(ticket)));

		byte[] combinedMessage = new XMLOutputter().outputString(doc).getBytes();
		return combinedMessage;
	}

}