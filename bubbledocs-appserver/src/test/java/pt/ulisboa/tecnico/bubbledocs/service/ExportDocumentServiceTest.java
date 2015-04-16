package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.StoreRemoteServices;


public class ExportDocumentServiceTest extends BubbleDocsServiceTest {
	
	// the tokens
    private String lars;
    private String lsam;
    private String lton;
    private String lmik;
    private int id;
    private String logana;
    
    private static final String USERNAME = "ars";
    private static final String UNAME = "arsdasdas";
    private static final String EMAIL = "ars@ars.pt";
    private static final String USERNAMER = "sam";
    private static final String USERNAMEL = "ton";
    private static final String USERNAMEFF = "mik";
    private static final String NAME = "folha";
    private static final String NOT_LOGGED_USERNAME = "amalhoa";
    private static final String NOT_LOGGED_NAME = "Ana Malhoa";
    private static final String NOT_LOGGED_EMAIL = "amalhoa@turbinada.pt";
    
    @Override
    public void populate4Test() {
    	User u = createUser(USERNAME, UNAME, EMAIL);
    	User u1 = createUser(USERNAMER, UNAME, EMAIL);
    	User u2 = createUser(USERNAMEL, UNAME, EMAIL);
    	User ana = createUser(NOT_LOGGED_USERNAME, NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);
    	createUser(USERNAMEFF, UNAME, EMAIL);
    	Spreadsheet s = createSpreadSheet(u, NAME, 10, 10);
    	id = s.getId();
    	u.modifyPermissions(u1.getUsername(), id, true, false);
    	u.modifyPermissions(u2.getUsername(), id, false, true);
    	lars = addUserToSession("ars");
    	lsam = addUserToSession("sam");
    	lton = addUserToSession("ton");
    	lmik = addUserToSession("mik");
    	logana = ana.getToken();
    	
    }
    
    @Test
    public void success() {
    	ExportDocumentService service = new ExportDocumentService(lars,id);
        service.execute();
        byte[] result = service.getResult();
        ImportSpreadsheetService service1 = new ImportSpreadsheetService(result);
        service1.execute();
        Spreadsheet s = service1.getResult();
        Spreadsheet s1 = getSpreadSheet(NAME);
        
        assertEquals(s.getName(), s1.getName());
        assertEquals(s.getLines(), s1.getLines());
        assertEquals(s.getColumns(), s1.getColumns());
        assertEquals(s.getOwner(), s1.getOwner());
        assertEquals(s.getId(), s1.getId());
    	}
    
    @Test
    public void success1() {
    	ExportDocumentService service = new ExportDocumentService(lsam,id);
        service.execute();
    }
    
    @Test
    public void success2() {
    	ExportDocumentService service = new ExportDocumentService(lton,id);
        service.execute();
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotLogged() {
    	ExportDocumentService service = new ExportDocumentService(logana,id);
    	service.execute();
    }
    
    @Test(expected = InvalidPermissionException.class)
    public void invalidPermission() {
    	ExportDocumentService service = new ExportDocumentService(lmik,id);
        service.execute();
    }
    
    @Test(expected = SpreadsheetDoesNotExistException.class)
    public void noSpreadSheet() {
    	ExportDocumentService service = new ExportDocumentService(lsam, 123456789);
    	service.execute();
    }
    
    @Mocked StoreRemoteServices remote1;
    @Test(expected = UnavailableServiceException.class)
    public void UnavailableService() {
    	
    	new Expectations() {{
    		remote1.storeDocument(anyString, anyString, withNotNull()); 
    		result = new RemoteInvocationException();
    	}};
    	
        new ExportDocumentService(lars, id).execute();
    }
}
