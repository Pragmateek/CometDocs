package org.cometdocs;

public class InvalidTokenException extends CometDocsException
{
	public InvalidTokenException(String message, Status status, String error)
	{
		super(message, status, error);
	}
}
