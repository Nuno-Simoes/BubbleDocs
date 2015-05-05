package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.BinaryFunction;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;

public class GetSpreadsheetContentServiceTest extends BubbleDocsServiceTest {
	
	private String tokfloyd;
	private String tokmanny;
	private String notLogged;
	private int spreadId;
	private int emptySpreadId;
	private static final int notValidSpreadId = 9000;
	
	private static final int SPREAD_LINES = 10;
	private static final int SPREAD_COLUMNS = 6;
	
	private static final String USERNAME = "floyd";
	private static final String UNAME = "Floyd Mayweather";
	private static final String EMAIL = "fmayweather@tmt.com";
	private static final String SPREAD_NAME = "How to read";
	private static final String EMPTY_SPREAD_NAME = "empy";
	
	private static final String USERNAME_NO_PERMISSIONS = "manny";
	private static final String UNAME_NO_PERMISSIONS = "Manny Pacquiao";
	private static final String EMAIL_NO_PERMISSIONS = "pacman@filipines.net";
	
	private static final String USERNAME_NOT_LOGGED = "Alberto";
	private static final String UNAME_NOT_LOGGED = "Alberto Jardim";
	private static final String EMAIL_NOT_LOGGED = "arberto@madeira.pt";
	
	private static final String PASSWORD = "";
	
	@Override
	public void populate4Test() {
		User u = createUser(USERNAME, UNAME, EMAIL);
		u.setPassword(PASSWORD);
		
		createUser(USERNAME_NOT_LOGGED, UNAME_NOT_LOGGED, EMAIL_NOT_LOGGED);
		
		User noPermissions = createUser(USERNAME_NO_PERMISSIONS, UNAME_NO_PERMISSIONS,
				EMAIL_NO_PERMISSIONS);
		noPermissions.setPassword(PASSWORD);
		
		Spreadsheet s = createSpreadSheet(u, SPREAD_NAME, SPREAD_LINES,
				SPREAD_COLUMNS);
		spreadId = s.getId();
		
		Literal four = new Literal(4);
		Literal twenty = new Literal(20);
		
		Cell cFour = s.getCell(1, 1);
		Cell cEmpty = s.getCell(6, 6);
		Cell cRef = s.getCell(3, 3);
		Cell cMult = s.getCell(9, 2);
		
		Reference ref1 = new Reference(cFour);
		Reference ref2 = new Reference(cEmpty);
		Reference ref3 = new Reference(cRef);
		Reference ref4 = new Reference(cMult);
		
		BinaryFunction add = new BinaryFunction(four, twenty);
		BinaryFunction mult = new BinaryFunction(ref1, 
				ref3);
		BinaryFunction sub = new BinaryFunction(ref4, 
				ref4);
		BinaryFunction div = new BinaryFunction(twenty, 
				ref1);
		
		s.setContent(1, 1, four);
		s.setContent(1, 3, twenty);
		s.setContent(3, 3, ref1);
		s.setContent(3, 4, ref2);
		s.setContent(5, 2, ref3);
		s.setContent(6, 4, add);
		s.setContent(9, 2, mult);
		s.setContent(8, 3, sub);
		s.setContent(9, 6, div);
		
		Spreadsheet s2 = createSpreadSheet(u, EMPTY_SPREAD_NAME, SPREAD_LINES,
				SPREAD_COLUMNS);
		emptySpreadId = s2.getId();
		
		tokfloyd = addUserToSession(USERNAME);
		tokmanny = addUserToSession(USERNAME_NO_PERMISSIONS);
	}
	
	@Test 
	public void success() {
		GetSpreadsheetContentIntegrator service = 
				new GetSpreadsheetContentIntegrator(tokfloyd, spreadId);
		service.execute();
		
		String [][] doc = service.getResult;
		
		int lines = doc.length;
        int columns = doc[0].length;
        
        assertEquals(SPREAD_LINES, lines);
        assertEquals(SPREAD_COLUMNS, columns);
		
		for (int i = 1; i<=lines; i++) {
			for(int j = 1; j<=columns; j++) {
				assertTrue(doc[i][j] instanceof String);
			}
		}
		
		assertEquals(doc[1][1], "4");
		assertEquals(doc[1][3], "20");
		assertEquals(doc[3][3], "4");
		assertEquals(doc[3][4], " ");
		assertEquals(doc[5][2], "4");
		assertEquals(doc[6][4], "24");
		assertEquals(doc[9][2], "16");
		assertEquals(doc[8][3], "0");
		assertEquals(doc[9][6], "5");
	}
	
	@Test 
	public void empyCellsAreEmptyStrings() {
		GetSpreadsheetContentIntegrator service = 
				new GetSpreadsheetContentIntegrator(tokfloyd, emptySpreadId);
		service.execute();
		
		String [][] doc = service.getResult;
		int lines = doc.length;
        int columns = doc[0].length;
		
		for (int i = 1; i<=lines; i++) {
			for(int j = 1; j<=columns; j++) {
				assertEquals(doc[i][j], " ");
			}
		}
	}
	
	@Test(expected = LoginBubbleDocsException.class)
	public void userNotLogged() {
		new GetSpreadsheetContentIntegrator(notLogged, spreadId).execute();
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void userDoestHavePermissions() {
		new GetSpreadsheetContentIntegrator(tokmanny, spreadId).execute();
	}
	
	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		new GetSpreadsheetContentIntegrator(notLogged, notValidSpreadId).execute();
	}
}
