package org.cometdocs;

public class CometDocsException extends Exception
{
	private String message;
	public String getMessage(){ return this.message; }
	public void setStatus(String message){ this.message = message; }
	
	private Status status;
	public Status getStatus(){ return this.status; }
	public void setStatus(Status status){ this.status = status; }
	
	private String error;
	public String getError(){ return this.error; }
	public void setError(String error){ this.error = error; }
	
	public CometDocsException(String message, Status status, String error)
	{
		super(String.format("%s (%s) [%s]", message, error, status));
		
		this.message = message;
		this.status = status;
		this.error = error;
	}
}
