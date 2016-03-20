package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegratorTest extends BubbleDocsIntegratorTest {

	private String userToken;
	private String notLoggedUserToken;

	private static final String VALID_USERNAME = "ars";
	private static final String VALID_NAME = "António Rito Silva";
	private static final String VALID_EMAIL = "ars@ars.com";

	private static final String NOT_LOGGED_USERNAME = "cbranco";
	private static final String NOT_LOGGED_NAME = "José Castelo Branco";
	private static final String NOT_LOGGED_EMAIL = "cbranco@vip.pt";

	private static final String PASSWORD = "random";

	@Override
	public void populate4Test() {
		User user = createUser(VALID_USERNAME, VALID_NAME, VALID_EMAIL);
		user.setPassword(PASSWORD);

		User notLoggedUser = createUser(NOT_LOGGED_USERNAME, 
				NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);
		notLoggedUser.setPassword(PASSWORD);

		userToken = addUserToSession(VALID_USERNAME);
		notLoggedUserToken = notLoggedUser.getToken();
	}

	@Test
	public void success() {
		
		boolean pass = false;
		RenewPasswordIntegrator integrator = 
				new RenewPasswordIntegrator(userToken);
		integrator.execute();
		
		User u = super.getUserFromUsername(VALID_USERNAME);
		
		if(u.getPassword() == null) {
			pass = true;
		}
		
		assertTrue("Local copy was not set invalid", pass);
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userNotLogged() {
		RenewPasswordIntegrator integrator = 
				new RenewPasswordIntegrator(notLoggedUserToken);
		integrator.execute();
	}

	@Test(expected = UnavailableServiceException.class)
	public void unavailableService(@Mocked final IDRemoteServices remote) {
		
		new Expectations() {{
			remote.renewPassword(anyString); 
			result = new RemoteInvocationException();
		}};
		
		RenewPasswordIntegrator integrator 
			= new RenewPasswordIntegrator(userToken);
		integrator.execute();
	}

}