package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.phonebook.exception.NameAlreadyExistsException;
import pt.tecnico.phonebook.service.CreatePersonService;
import pt.tecnico.phonebook.service.PhoneBookService;
import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;

public class CreateUserTest extends AbstractServiceTest {
	
	protected void initializeDomain() {
		RootUser r = RootUser.getInstance();
		r.addUser("ns", "Nuno", "ns", "ns");
	}
	
	@Test
	public void success() {
		CreateUserService service = new CreateUserService("rp", "Rita", "rp");
		service.execute();
		
		assertTrue("user was not created", PortalService.getPortal().findUser("rp"));
	}
	
	@Test(expected = UserAlreadyExistException)
	public void unauthorizedUserCreation() {
		CreatePersonService service = new CreateUserService("ns", "Nuno", "ns");
		service.execute();
	}
}