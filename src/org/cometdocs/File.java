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
		super();
		
		content = new byte[0];
	}
	
	public File(String nameWithExtension)
	{
		super(nameWithExtension);
		
		content = new byte[0];
	}
}
