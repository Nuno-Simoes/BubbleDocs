package id.ws.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

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

@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.id.ws.SDId", wsdlLocation = "SD-ID.1_1.wsdl", name = "SdId", portName = "SDIdImplPort", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:id:ws", serviceName = "SDId")
public class SDIdImpl implements SDId {

	List<User> user = new ArrayList<User>();

	public SDIdImpl() {
		User alice = new User("alice", "Aaa1", "alice@tecnico.pt");
		User bruno = new User("bruno", "Bbb2", "bruno@tecnico.pt");
		User carla = new User("carla", "Ccc3", "carla@tecnico.pt");
		User duarte = new User("duarte", "Ddd4", "duarte@tecnico.pt");
		User eduardo = new User("eduardo", "Eee5", "eduardo@tecnico.pt");

		user.add(alice);
		user.add(bruno);
		user.add(carla);
		user.add(duarte);
		user.add(eduardo);

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
		user.add(u);
	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception {

		User us=null;

		for (User u : user) {
			if (u.getUserId().equals(userId)) {
				us = u;
				break;
			}
		}

		if (us == null) {
			UserDoesNotExist udne = new UserDoesNotExist();
			udne.setUserId(userId);
			throw new UserDoesNotExist_Exception("Username Does Not Exist",
					udne);
		} else {
			us.setPassword();
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

		byte[] pass = { 0 };
		try {
			String s = new String(reserved);
			for (User u : user) {
				if (u.getUserId().equals(userId)) {
					if (u.getPassword().equals(s)) {
						pass[0] = 1;
						return pass;
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
		}

		AuthReqFailed arf = new AuthReqFailed();
		arf.setReserved(reserved);
		throw new AuthReqFailed_Exception("Authentication Failed", arf);

	}

}