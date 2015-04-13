package pt.ulisboa.tecnico.bubbledocs.service.remote;

import pt.ulisboa.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.ulisboa.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.ulisboa.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exception.RemoteInvocationException;

public class IDRemoteServices {
    public void createUser(String username, String email)
           throws InvalidUsernameException, DuplicateUsernameException,
           DuplicateEmailException, InvalidEmailException,
           RemoteInvocationException {
       // TODO : the connection and invocation of the remote service
}
    public void loginUser(String username, String password)
           throws LoginBubbleDocsException, RemoteInvocationException {
       // TODO : the connection and invocation of the remote service
}
    public void removeUser(String username)
           throws LoginBubbleDocsException, RemoteInvocationException {
       // TODO : the connection and invocation of the remote service
}
   public void renewPassword(String username)
           throws LoginBubbleDocsException, RemoteInvocationException {
       // TODO : the connection and invocation of the remote service
} }
