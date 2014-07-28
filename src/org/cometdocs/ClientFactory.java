package org.cometdocs;

public class ClientFactory
{
	public static Client getClient()
	{
		return new ClientImpl();
	}
}
