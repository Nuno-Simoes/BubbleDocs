package id.ws.impl;

import id.ws.uddi.UDDINaming;
import id.ws.handler.RelayClientHandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.ws.BindingProvider;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

public class ClientIdMain {

	public static final String TOKEN = "client";

	public static final String CLASS_NAME = ClientIdMain.class.getSimpleName();
	
	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n",
					ClientIdMain.class.getName());
			return;
		}
		
		SecretKeySpec originalKey = null;
		byte[] ticketToServer=null;
		
		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];
		String name1 = args[3];
		String url1 = args[4];

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);
		
		System.out.printf("Looking for '%s'%n", name1);
		String endpointAddress1 = uddiNaming.lookup(name1);
		
		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
		
		if (endpointAddress1 == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress1);
		}

		System.out.println("Creating stub ...");
		SDId_Service service = new SDId_Service();
		SDId port = service.getSDIdImplPort();

		SDStore_Service serviceSD = new SDStore_Service();
		SDStore port1 = serviceSD.getSDStoreImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider
				.getRequestContext();
		
		// *** #1 ***
        // put token in request context
        String initialValue = TOKEN;
        System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, initialValue);
        requestContext.put(RelayClientHandler.REQUEST_PROPERTY, initialValue);
        
        // set endpoint address
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

		BindingProvider bindingProvider1 = (BindingProvider) port1;
		Map<String, Object> requestContext1 = bindingProvider1
				.getRequestContext();
		requestContext1.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress1);

		while (true) {
			System.out.println("Insira um numero para seleccionar a opcao \n 1:Create User \n 2:Remove User \n 3:Renew Password \n 4:Request Authentication \n 5:Create Document \n 6:List Documents \n 7:Exit");
			BufferedReader bufferRead = new BufferedReader(
					new InputStreamReader(System.in));
			String s = bufferRead.readLine();
			String k;
			if (s.equals("1")) {
				System.out.println("Create new user: ");
				System.out.println("Insert userId: ");
				s = bufferRead.readLine();
				System.out.println("Insert email: ");
				k = bufferRead.readLine();
				port.createUser(s, k);
			}
			if (s.equals("2")) {
				System.out.println("Remove user: ");
				System.out.println("Insert userId: ");
				s = bufferRead.readLine();
				port.removeUser(s);
			}
			if (s.equals("3")) {
				System.out.println("Renew password: ");
				System.out.println("Insert userId: ");
				s = bufferRead.readLine();
				port.renewPassword(s);
			}
			if (s.equals("4")) {
				// parte do request
				System.out.println("Insert userId: ");
				s = bufferRead.readLine();
				System.out.println(s);
				System.out.println("Insert a random number: ");
				byte[] reserved = bufferRead.readLine().getBytes();
				String userPass = bufferRead.readLine();
				byte[] finalProduct = port.requestAuthentication(s,
						("sdStore;" + reserved).getBytes());

				// parte criar doc3
				String finalString = new String(finalProduct);
				Document finalXml = loadXML(finalString.getBytes());
				Element root = finalXml.getRootElement();
				// doc3 part1Message
				String part1Message = root.getAttributeValue("part1Message");
				// doc3 part2Message
				String part2Message = root.getAttributeValue("part2Message");
				// criar userKey da password
				SecretKeySpec userKey = generatePasswordKey(userPass);
				// parte criar doc1
				String part1Deciphered = decipher(userKey,
						parseBase64Binary(part1Message));
				System.out.println("\n!!!!!!!!!!! Xml Document deciphered part 1 !!!!!!!!!!!\n"
						+ part1Deciphered + "\n");
				Document part1Xml = loadXML(part1Deciphered.getBytes());
				Element rootDoc1 = part1Xml.getRootElement();
				// sacar sessionKey da parte1Xml
				String sessionKey = rootDoc1.getAttributeValue("sessionKey");
				byte[] deKey = parseBase64Binary(sessionKey);
				System.out.println("\n!!!!!!!!!!! Session key !!!!!!!!!!!\n " + deKey + "\n");
				originalKey = new SecretKeySpec(deKey, 0,
						deKey.length, "AES");
				System.out.println("originalKey " + originalKey);
				// parte criar doc2
				ticketToServer = parseBase64Binary(part2Message);
				System.out.println("\n!!!!!!!!!!! Ticket from server !!!!!!!!!!!\n"
						+ ticketToServer + "\n");
			}
			if (s.equals("5")) {
				System.out.println("Insert userId: ");
				s=bufferRead.readLine();
				
				Element root = new Element("root");
				Document doc = new Document();
				doc.setRootElement(root);
				byte[] authent = authenticator(s,originalKey);
				root.setAttribute(new Attribute("ticket", ticketToServer.toString()));
				root.setAttribute(new Attribute("authenticator", authent.toString()));
				String allExceptRequest = new XMLOutputter().outputString(doc);
				
				DocUserPair pair = new DocUserPair();
				pair.setUserId(s);
				pair.setDocumentId(allExceptRequest);
				// make remote call
		        System.out.printf("Remote call to %s ...%n", url);
		        port1.createDoc(pair);
		        
		        // access response context
		        Map<String, Object> responseContext = bindingProvider.getResponseContext();

		        // *** #12 ***
		        // get token from response context
		        String finalValue = (String) responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
		        System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue);
				
			}
			if (s.equals("6")) {

				System.out.println("Insert userId: ");
				s=bufferRead.readLine();
				System.out.printf("Remote call to %s ...%n", url);
				List<String> list = port1.listDocs(s);
				for(Iterator<String> i = list.iterator(); i.hasNext(); ) {
				    String item = i.next();
				    System.out.println(item);
				}
				// access response context
		        Map<String, Object> responseContext = bindingProvider.getResponseContext();

		        // *** #12 ***
		        // get token from response context
		        String finalValue = (String) responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
		        System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue);
				
			}
			if(s.equals("7")){
				System.out.println("Goodbye!");
				break;
			}
		}
	}

	public static SecretKeySpec generatePasswordKey(String password)
			throws NoSuchAlgorithmException {
		MessageDigest sha = null;
		sha = MessageDigest.getInstance("SHA-1");
		byte[] key = sha.digest(password.getBytes());
		key = Arrays.copyOf(key, 16);
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		return secretKey;
	}

	public static Document loadXML(byte[] doc) throws Exception {
		Document jdomDoc;

		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		jdomDoc = builder.build(new ByteArrayInputStream(doc));

		return jdomDoc;
	}

	public static String decipher(SecretKeySpec key, byte[] forDecipher)
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

	public static byte[] authenticator(String clientId, SecretKeySpec sessionKey)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		Date current = new Date();
		Element root = new Element("root");
		Document doc = new Document();
		doc.setRootElement(root);
		root.setAttribute(new Attribute("clientId", clientId));
		root.setAttribute(new Attribute("currentTime", current.toString()));
		String authent = new XMLOutputter().outputString(doc);
		System.out.println("xml " + authent);
		byte[] authenticatorFinal = cipher(sessionKey, authent);
		return authenticatorFinal;
	}

	public static byte[] cipher(SecretKeySpec key, String forCipher)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		IvParameterSpec iv = new IvParameterSpec("GUFHVNSMIORLDMTP".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] reserved2 = forCipher.getBytes();
		byte[] cipherPwBytes = cipher.doFinal(reserved2);
		return cipherPwBytes;
	}
}
