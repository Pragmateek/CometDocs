package org.cometdocs;

public class InvalidTokenException extends CometDocsException
{
	public InvalidTokenException(String message, Status status)
	{
		super(message, status);
	}
}
