package org.cometdocs;

public class FileInfo
{
    private long id;
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	
    private String name;
    public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
    private String extension;
    public String getExtension() { return extension; }
	public void setExtension(String extension) { this.extension = extension; }
	
    private long size;
	public long getSize() { return size; }
	public void setSize(long size) { this.size = size; }
	
    private Boolean hasConversions;
    public Boolean hasConversions() { return hasConversions; }
	public void hasConversions(Boolean hasConversions) { this.hasConversions = hasConversions; }

    @Override
    public int hashCode()
    {
        return (int)id;
    }

    @Override
    public boolean equals(Object obj)
    {
    	if (!(obj instanceof FileInfo)) return false;
    	
        FileInfo other = (FileInfo)obj;

        return other != null && other.id == this.id;
    }	
}
