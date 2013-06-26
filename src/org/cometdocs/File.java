package org.cometdocs;

public class File extends FileInfo
{
    private byte[] content;
	public byte[] getContent() { return content; }
	public void setContent(byte[] content)
	{
		if (content == null) throw new NullPointerException("Content cannot be null; for empty files provide an empty array or do not provide anything!");
		
		this.content = content;
	}

	public File()
	{
		this(null);
	}
	
    public File(String nameWithExtension)
    {
        if (nameWithExtension != null)
        {
            String[] tokens = nameWithExtension.split("\\.");

            String name;
            
            if (tokens.length >= 2)
            {
                name = tokens[0];
                for (int i = 1; i < tokens.length - 1; ++i)
                {
                    name += "." + tokens[i];
                }

                setExtension(tokens[tokens.length - 1]);
            }
            else
            {
                name = tokens[0];
            }
            setName(name);
        }
        
        content = new byte[0];
    }
}
