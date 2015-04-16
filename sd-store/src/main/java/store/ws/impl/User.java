package store.ws.impl;

public class User {
	
	Repository repository;
	String userId;
	
	public User (String userId) {
		this.userId = userId;
		this.repository = null;
	}
	
	public Repository getRepository () {
		return this.repository;
	}
	
	public String getUserId () {
		return this.userId;
	}
	
	public void createRepository (String documentId) {
		this.repository = new Repository (this, new Document (documentId));
	}
	
}