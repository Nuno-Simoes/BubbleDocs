package pt.ulisboa.tecnico.bubbledocs.service;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ExportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbledocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ExportDocumentService extends PortalService {
    private byte[] docXML;
    private String userToken;
    private int docId;

    public byte[] getDocXML() {
    	return docXML;
    }

    public ExportDocumentService(String userToken, int docId) {
    	this.userToken = userToken;
    	this.docId = docId;
    }

    @Override
    protected void dispatch() throws BubbledocsException, ExportDocumentException,
    	LoginBubbleDocsException, InvalidPermissionException {
    	
    	User u = super.getUser(userToken);
    	Portal p = Portal.getInstance();
    	Spreadsheet s = p.findSpreadsheet(this.docId);
    	Permission perm = u.findPermission(u.getUsername(), s.getId());
    	
    	if(p.isOwner(u, s) || perm.getWrite() || perm.getRead()){
	    	org.jdom2.Document jdomDoc = new org.jdom2.Document();

	        jdomDoc.setRootElement(s.exportToXML());
	
	        XMLOutputter xml = new XMLOutputter();
	        try {
	            this.docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	            throw new ExportDocumentException();
	        }
    	} else {
    		throw new InvalidPermissionException(u.getUsername());
    	}
    	
      	String userName = u.getUsername();
    	String docName = s.getName();
    	
    	try {
    		StoreRemoteServices service = new StoreRemoteServices();
    		service.storeDocument(userName, docName, docXML);
    	} catch (RemoteInvocationException rie){
    		throw new UnavailableServiceException();
    	}
    }

    public final byte[] getResult() {
        return this.docXML;
    }
    
}