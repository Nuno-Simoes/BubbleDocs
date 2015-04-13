package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class CreateUserTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "António Rito Silva");
        root = addUserToSession("root");
        ars = addUserToSession("ars");
    }
    
    @Test
    public void success() {
        CreateUserService service = new CreateUserService(root, USERNAME_DOES_NOT_EXIST, "jose",
                "José Ferreira");
        service.execute();

	// User is the domain class that represents a User
        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals("jose", user.getPassword());
        assertEquals("José Ferreira", user.getName());
    }
    
    
    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
        CreateUserService service = new CreateUserService(root, USERNAME, "jose",
                "José Ferreira");
        service.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername() {
        CreateUserService service = new CreateUserService(root, "", "jose", "José Ferreira");
        service.execute();
    }
	
    @Test(expected = InvalidPermissionException.class)
    public void unauthorizedUserCreation() {
        CreateUserService service = new CreateUserService(ars, USERNAME_DOES_NOT_EXIST, "jose",
                "José Ferreira");
        service.execute();
    }
	
    @Test(expected = LoginBubbleDocsException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(root);
        CreateUserService service = new CreateUserService(root, USERNAME_DOES_NOT_EXIST, "jose",
                "José Ferreira");
        service.execute();
    }
}
