package id.ws.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.jws.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.annotation.Resource;
import javax.jws.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import id.ws.impl.handler.*;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.id.ws.SDId",
	wsdlLocation = "SD-ID.1_1.wsdl",
	name = "SdId",
	portName = "SDIdImplPort",
	targetNamespace = "urn:pt:ulisboa:tecnico:sdis:id:ws",
	serviceName = "SDId")
@HandlerChain(file="handler-chain.xml")
public class SDIdImpl implements SDId {

	public static final String TOKEN = "server";
	public static final String CLASS_NAME = SDIdImpl.class.getSimpleName();

	@Resource
	private WebServiceContext webServiceContext;

	List<User> user = new ArrayList<User>();

	public SDIdImpl() {
		User alice = new User("alice", "Aaa1", "alice@tecnico.pt");
		User bruno = new User("bruno", "Bbb2", "bruno@tecnico.pt");
		User carla = new User("carla", "Ccc3", "carla@tecnico.pt");
		User duarte = new User("duarte", "Ddd4", "duarte@tecnico.pt");
		User eduardo = new User("eduardo", "Eee5", "eduardo@tecnico.pt");
		User sdStore = new User("sdStore", "sdstore1", "sdStore@sdstore.com");

		user.add(alice);
		user.add(bruno);
		user.add(carla);
		user.add(duarte);
		user.add(eduardo);
		user.add(sdStore);
	}

	private String decodeTicket(String document) throws Exception {
		Document received = loadXML(document.getBytes());
		Element root = received.getRootElement();
		String ticket = root.getAttributeValue("ticket");
		return ticket;
	}

	private String decodeAuthenticator(String document) throws Exception {
		Document received = loadXML(document.getBytes());
		Element root = received.getRootElement();
		String authenticator = root.getAttributeValue("authenticator");
		return authenticator;
	}

	private static Document loadXML(byte[] doc) throws Exception {
		Document jdomDoc;

		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		jdomDoc = builder.build(new ByteArrayInputStream(doc));

		return jdomDoc;
	}

	public boolean checkRequest(Document doc) throws Exception {
		// retrieve message context
		MessageContext messageContext = webServiceContext.getMessageContext();

		// *** #6 ***
		// get token from message context
		String propertyValue = (String) messageContext
				.get(RelayServerHandler.REQUEST_PROPERTY);
		System.out.printf("%s got token '%s' from response context%n",
				CLASS_NAME, propertyValue);
		
		String receivedTicket = decodeTicket(propertyValue);
		String receivedAuthenticator = decodeAuthenticator(propertyValue);
		Document ticket = loadXML(receivedTicket.getBytes());
		Element rootTicket = ticket.getRootElement();
		String ticketDate = rootTicket.getAttributeValue("expiration");
		Document authenticator = loadXML(receivedAuthenticator.getBytes());
		Element rootAuthenticator = authenticator.getRootElement();
		String authenticatorDate = rootAuthenticator
				.getAttributeValue("currentTime");
		SimpleDateFormat parserSDF = new SimpleDateFormat(
				"EEE MMM d HH:mm:ss zzz yyyy");
		Date ticketTime = parserSDF.parse(ticketDate);
		Date authenticatorTime = parserSDF.parse(authenticatorDate);
		boolean after = authenticatorTime.after(ticketTime);
		// server processing
        String result = String.format("The result is %b!", after);
        System.out.printf("Result: %s%n", result);
		// *** #7 ***
		// put token in message context
		String newValue = propertyValue + "," + TOKEN;
		System.out.printf("%s put token '%s' on request context%n", CLASS_NAME,
				TOKEN);
		messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, newValue);
		return after;

	}

	public User findUser(String userId) {
		for (User u : user) {
			if (u.getUserId().equals(userId)) {
				return u;
			}
		}
		return null;
	}

	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {

		try {
			if (userId.equals("") || userId.equals(null)) {
				InvalidUser iu = new InvalidUser();
				iu.setUserId(userId);
				throw new InvalidUser_Exception("Wrong Username", iu);
			}
		} catch (NullPointerException npe) {
			InvalidUser iu = new InvalidUser();
			iu.setUserId(userId);
			throw new InvalidUser_Exception("Wrong Username", iu);
		}

		try {
			if (emailAddress.equals("") || emailAddress.equals(null)) {
				InvalidEmail ie = new InvalidEmail();
				ie.setEmailAddress(emailAddress);
				throw new InvalidEmail_Exception("Wrong Email Address", ie);
			}
		} catch (NullPointerException npe) {
			InvalidEmail ie = new InvalidEmail();
			ie.setEmailAddress(emailAddress);
			throw new InvalidEmail_Exception("Wrong Email Address", ie);
		}

		int charCount = 0;

		for (int i = 0; i < emailAddress.length(); i++) {
			if (emailAddress.charAt(i) == '@') {
				charCount++;
			}
		}

		if (charCount != 1) {
			InvalidEmail ie = new InvalidEmail();
			ie.setEmailAddress(emailAddress);
			throw new InvalidEmail_Exception("Wrong Email Address", ie);
		}

		String[] parts = emailAddress.split("@");

		try {
			if (parts[0] == null || parts[0].equals("") || parts[1] == null
					|| parts[1].equals("")) {
				InvalidEmail ie = new InvalidEmail();
				ie.setEmailAddress(emailAddress);
				throw new InvalidEmail_Exception("Wrong Email Address", ie);
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			InvalidEmail ie = new InvalidEmail();
			ie.setEmailAddress(emailAddress);
			throw new InvalidEmail_Exception("Wrong Email Address", ie);
		}

		for (User u : user) {
			if (u.getUserId().equals(userId)) {
				UserAlreadyExists uae = new UserAlreadyExists();
				uae.setUserId(userId);
				throw new UserAlreadyExists_Exception(
						"Username Already Exists", uae);
			}
			if (u.getEmailAddress().equals(emailAddress)) {
				EmailAlreadyExists eae = new EmailAlreadyExists();
				eae.setEmailAddress(emailAddress);
				throw new EmailAlreadyExists_Exception(
						"Email Address Already Exists", eae);
			}
		}

		User u = new User(userId, null, emailAddress);
		u.setPassword();
		System.out.println("Password de " + u.getUserId() + ": "
				+ u.getPassword());
		user.add(u);

	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception {

		User us = findUser(userId);

		if (us == null) {
			UserDoesNotExist udne = new UserDoesNotExist();
			udne.setUserId(userId);
			throw new UserDoesNotExist_Exception("Username Does Not Exist",
					udne);
		} else {
			us.setPassword();
			System.out.println("Nova password de " + us.getUserId() + ": "
					+ us.getPassword());
		}

	}

	public void removeUser(String userId) throws UserDoesNotExist_Exception {

		for (User u : user) {
			if (u.getUserId().equals(userId)) {
				user.remove(u);
				return;
			}
		}

		UserDoesNotExist udne = new UserDoesNotExist();
		udne.setUserId(userId);
		throw new UserDoesNotExist_Exception("Username Does Not Exist", udne);

	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {
		User u = findUser(userId);
		String string = new String(reserved);
		String[] parts = string.split(";");
		String part1 = parts[0];
		String part2 = parts[1];
		User s = findUser(part1);

		Kerberos kerb = Kerberos.getInstance();
		try {
			String password = u.getPassword();
			SecretKeySpec clientKey = kerb.generatePasswordKey(u.getPassword());
			SecretKeySpec serverKey = kerb.generatePasswordKey(s.getPassword());
			SecretKeySpec sessionKey = kerb.generateSessionKey();
			System.out.println("sessionKey" + sessionKey);
			byte[] message = kerb.generateMessage(sessionKey, part2, clientKey);
			byte[] ticket = kerb.generateTicket(u.getUserId(), s.getUserId(),
					sessionKey, serverKey);
			byte[] combined = kerb.combineMessage(ticket, message);
			for (User us : user) {
				if (us.getUserId().equals(userId)) {
					if (us.getPassword().equals(password)) {
						System.out.println(" Autenticado ");
						return combined;
					} else {
						AuthReqFailed arf = new AuthReqFailed();
						arf.setReserved(reserved);
						throw new AuthReqFailed_Exception(
								"Authentication Failed", arf);
					}
				}
			}
		} catch (NullPointerException npe) {
			AuthReqFailed arf = new AuthReqFailed();
			arf.setReserved(reserved);
			throw new AuthReqFailed_Exception("Authentication Failed", arf);
		} catch (NoSuchAlgorithmException e) {
			AuthReqFailed arf = new AuthReqFailed();
			arf.setReserved(reserved);
			throw new AuthReqFailed_Exception("Authentication Failed", arf);
		} catch (InvalidKeyException e) {
			AuthReqFailed arf = new AuthReqFailed();
			arf.setReserved(reserved);
			throw new AuthReqFailed_Exception("Authentication Failed", arf);
		} catch (NoSuchPaddingException e) {
			AuthReqFailed arf = new AuthReqFailed();
			arf.setReserved(reserved);
			throw new AuthReqFailed_Exception("Authentication Failed", arf);
		} catch (IllegalBlockSizeException e) {
			AuthReqFailed arf = new AuthReqFailed();
			arf.setReserved(reserved);
			throw new AuthReqFailed_Exception("Authentication Failed", arf);
		} catch (BadPaddingException e) {
			AuthReqFailed arf = new AuthReqFailed();
			arf.setReserved(reserved);
			throw new AuthReqFailed_Exception("Authentication Failed", arf);
		} catch (IOException e) {
			AuthReqFailed arf = new AuthReqFailed();
			arf.setReserved(reserved);
			throw new AuthReqFailed_Exception("Authentication Failed", arf);
		} catch (InvalidAlgorithmParameterException e) {
			AuthReqFailed arf = new AuthReqFailed();
			arf.setReserved(reserved);
			throw new AuthReqFailed_Exception("Authentication Failed", arf);
		}

		AuthReqFailed arf = new AuthReqFailed();
		arf.setReserved(reserved);
		throw new AuthReqFailed_Exception("Authentication Failed", arf);

	}

}