package store.ws.impl;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.util.ArrayList;
import java.util.List;

public class Repository {
	
	User user;
	List<Document> documents;
	int size;
	int capacity;
	
	public Repository (User user, Document document) {
		this.size = 0;
		this.capacity = 10*1024;
		this.user = user;
		document.setContents(parseBase64Binary(""));
		this.documents = new ArrayList<Document>();
		documents.add(document);
	}
	
	public User getUser () {
		return this.user;
	}
	
	public List<Document> getDocuments () {
		return this.documents;
	}
	
	public int getSize () {
		return this.size;
	}
	
	public int getCapacity () {
		return this.capacity;
	}
	
	public void setSize (int size) {
		this.size = size;
	}
	
	public void setCapacity (int capacity) {
		this.capacity = capacity;
	}
	
	public List<String> listDocuments () {
		List<String> list = new ArrayList<String>();
		
		for (Document d : this.documents) {
			list.add(d.getDocumentId());
		}
		
		return list;
	}
	
	public boolean documentExists (String documentId) {
		
		for (Document d : this.documents) {
			if (d.getDocumentId().equals(documentId)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addDocument (String documentId) {
		this.documents.add(new Document(documentId));
	}
	
	public Document getDocument (String documentId) {
		for (Document d : this.documents) {
			if (d.getDocumentId().equals(documentId)) {
				return d;
			}
		}
		
		return null;
	}
	
	public byte[] readDocument (String documentId) {
		Document document = getDocument(documentId);
		return document.getContents();
	}
	
	public void writeDocument (String documentId, byte[] contents) {
		Document document = getDocument(documentId);
		document.setContents(contents);
	}
	
}