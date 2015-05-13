package pt.ulisboa.tecnico.bubbledocs.service.remote;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import id.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

public class IDRemoteServices {

	public static IDRemoteServices idRemote = null;

	public static IDRemoteServices getInstance() {
		if (idRemote == null) {
			idRemote = new IDRemoteServices();
		}
		return idRemote;
	}

	static private String uddiURL = "http://localhost:8081";
	static private String name = "SdId";
	static protected SDId port;

	public IDRemoteServices() {
		try {
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			String endpointAddress = uddiNaming.lookup(name);
			SDId_Service service = new SDId_Service();
			port = service.getSDIdImplPort();
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider
					.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}

	public void createUser(String username, String email)
			throws LoginBubbleDocsException, DuplicateUsernameException,
			DuplicateEmailException, InvalidEmailException,
			RemoteInvocationException {
		try {
			port.createUser(username, email);
		} catch (EmailAlreadyExists_Exception e) {
			throw new DuplicateUsernameException();
		} catch (InvalidEmail_Exception e) {
			throw new InvalidEmailException();
		} catch (InvalidUser_Exception e) {
			throw new LoginBubbleDocsException();
		} catch (UserAlreadyExists_Exception e) {
			throw new DuplicateUsernameException();
		}
	}

	public void loginUser(String username, String password)
			throws LoginBubbleDocsException, RemoteInvocationException {
		try {
			port.requestAuthentication(username, ("sdStore;" + password).getBytes());
		} catch (AuthReqFailed_Exception e) {
			throw new LoginBubbleDocsException();
		}
	}

	public void removeUser(String username) throws LoginBubbleDocsException,
			RemoteInvocationException {
		try {
			port.removeUser(username);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}
	}

	public void renewPassword(String username) throws LoginBubbleDocsException,
			RemoteInvocationException {
		try {
			port.renewPassword(username);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}
	}

}