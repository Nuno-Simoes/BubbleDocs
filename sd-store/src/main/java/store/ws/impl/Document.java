package store.ws.impl;

public class Document {
	
	public String documentId;
	public byte[] contents;	
	
	public Document (String documentId) {
		this.documentId = documentId;
	}
	
	public String getDocumentId () {
		return this.documentId;
	}
	
	public byte[] getContents() {
		return this.contents;
	}
	
	public void setContents (byte[] contents) {
		this.contents = contents;
	}
	
}