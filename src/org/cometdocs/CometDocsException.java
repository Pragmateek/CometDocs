package org.cometdocs;

/**
 * Represents an Exception raised by the CometDocs API.
 */
public class CometDocsException extends Exception
{
	/**
	 * The corresponding Response's message.
	 */
	private String message;
	public String getMessage(){ return this.message; }
	public void setStatus(String message){ this.message = message; }
	
	/**
	 * The corresponding Response's status.
	 */
	private Status status;
	public Status getStatus(){ return this.status; }
	public void setStatus(Status status){ this.status = status; }
	
	/**
	 * The corresponding Response's error message.
	 */
	private String error;
	public String getError(){ return this.error; }
	public void setError(String error){ this.error = error; }
	
	/**
	 * Create a new instance of CometDocsException using the error data sent by the CometDocs web API.
	 * @param message
	 * @param status
	 * @param error
	 */
	public CometDocsException(String message, Status status, String error)
	{
		super(String.format("%s (%s) [%s]", message, error, status));
		
		this.message = message;
		this.status = status;
		this.error = error;
	}
}
