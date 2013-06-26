package org.cometdocs;

public class CometDocsException extends Exception
{
	private Status status;
	public Status getStatus(){ return this.status; }
	public void setStatus(Status status){ this.status = status; }
	
	public CometDocsException(String message, Status status)
	{
		super(message);
	}
}
