package org.cometdocs;

class Response
{
    private Status status;
	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }
	
	private String message;
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
	private String error;
	public String getError() { return error; }
	public void setError(String error) { this.error = error; }
}
