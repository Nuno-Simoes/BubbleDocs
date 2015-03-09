package pt.ulisboa.tecnico.bubbledocs.domain;

public class User extends User_Base {
    
    public User() {
        super();
    }
   
   public User(String username, String name, String password) {
	   super();
	   this.init(username, name, password);
   }
   
   protected void init(String username, String name, String password) {
	   this.setUsername(username);
	   this.setName(name);
	   this.setPassword(password);
   }
    
}
