package org.cometdocs;

public class ConversionType
{
    private String from;
    public String getFrom(){ return from; }
	public void setFrom(String from) { this.from = from; }

	private String to;
	public String getTo() { return to; }
	public void setTo(String to) { this.to = to; }

    public ConversionType(String from, String to)
    {
        this.from = from;
        this.to = to;
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
    	if (!(obj instanceof ConversionType)) return false;
    	
        ConversionType other = (ConversionType)obj;

        return other != null && other.toString().equals(this.toString());
    }

    @Override
    public String toString()
    {
        return from + "2" + to;
    }

    public static ConversionType fromString(String s)
    {
        String[] tokens = s.split("2");

        return new ConversionType(tokens[0], tokens[1]);
    }
}
