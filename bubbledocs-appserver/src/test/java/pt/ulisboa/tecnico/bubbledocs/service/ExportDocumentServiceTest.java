package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;


public class ExportDocumentServiceTest extends BubbleDocsServiceTest {
	
	// the tokens
    private String ars;
    private String sam;
    private String ton;
    private String mik;
    private int id;
    
    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String USERNAMER = "sam";
    private static final String USERNAMEL = "ton";
    private static final String USERNAMEFF = "mik";
    private static final String NAME = "folha";
    
    @Override
    public void populate4Test() {
    	User u = createUser(USERNAME, PASSWORD, "António");
    	User u1 = createUser(USERNAMER, PASSWORD, "Sam");
    	User u2 = createUser(USERNAMEL, PASSWORD, "Ton");
    	createUser(USERNAMEFF, PASSWORD, "Mik");
    	Spreadsheet s = createSpreadSheet(u, NAME, 10, 10);
    	id = s.getId();
    	u.modifyPermissions(u1.getUsername(), id, true, false);
    	u.modifyPermissions(u2.getUsername(), id, false, true);
    	ars = addUserToSession("ars");
    	sam = addUserToSession("sam");
    	ton = addUserToSession("ton");
    	mik = addUserToSession("mik");
    	
    }
    
    @Test
    public void success() {
    	ExportDocumentService service = new ExportDocumentService(ars,id);
        service.execute();
        byte[] result = service.getResult();
        ImportSpreadsheetService service1 = new ImportSpreadsheetService(result);
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
    	ExportDocumentService service = new ExportDocumentService(sam,id);
        service.execute();
    }
    
    @Test
    public void success2() {
    	ExportDocumentService service = new ExportDocumentService(ton,id);
        service.execute();
    }
    
    
    @Test(expected = InvalidPermissionException.class)
    public void invalidPermission() {
    	ExportDocumentService service = new ExportDocumentService(mik,id);
        service.execute();
    }
    
    
	

}
