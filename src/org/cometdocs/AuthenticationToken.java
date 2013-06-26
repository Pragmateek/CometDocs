package org.cometdocs;

public class AuthenticationToken
{
    private String value;
    public String getValue() { return value; }
	public void setValue(String value) { this.value = value; }

	public AuthenticationToken(String value)
    {
        this.value = value;
    }
}
