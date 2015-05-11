package store.ws.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;

public class SecureStore {
	
	private FrontEnd frontEnd = FrontEnd.getInstance();
	private Cipher aes;
	private SecretKey key;
	private IvParameterSpec iv;
		
	public SecureStore () {
		this.key = generateKey();
		this.iv = generateIv();
	}
	
	private SecretKey generateKey() {
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
	
	private IvParameterSpec generateIv() {
		final int AES_KEYLENGTH = 128;
		byte[] iv = new byte[AES_KEYLENGTH/8];
		IvParameterSpec ivParam = new IvParameterSpec(iv);
		return ivParam;
	}
	
	public SecretKey getKey() {
		return this.key;
	}
	
	public IvParameterSpec getIv() {
		return this.iv;
	}
	
	public void store (DocUserPair docUserPair, byte[] contents) {
		
		byte[] cipherText = null;
		
		try {
			aes = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			aes.init(Cipher.ENCRYPT_MODE, getKey(), getIv());
			cipherText = aes.doFinal(contents);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch(InvalidAlgorithmParameterException iape) {
			iape.printStackTrace();
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		}
		
		frontEnd.store(docUserPair, cipherText);
	}
	
	public byte[] load (DocUserPair docUserPair) {
		
		byte[] cipherText = frontEnd.load(docUserPair);
		byte[] decryptedText = null;
		
		try {
			aes = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			aes.init(Cipher.DECRYPT_MODE, getKey(), getIv());
			decryptedText = aes.doFinal(cipherText);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (InvalidAlgorithmParameterException iape) {
			iape.printStackTrace();
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		}
		
		return decryptedText;
	}

}
