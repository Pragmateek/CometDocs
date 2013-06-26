package org.cometdocs;

public class Conversion
{
    private int id;
    public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
    private int parentId;
    public int getParentId() { return parentId; }
	public void setParentId(int parentId) { this.parentId = parentId; }
	
    private String extension;
    public String getExtension() { return extension; }
	public void setExtension(String extension) { this.extension = extension; }
	
    private int size;
    public int getSize() { return size; }
	public void setSize(int size) { this.size = size; }
	
    private ConversionType type;	
	public ConversionType getType() { return type; }
	public void setType(ConversionType type) { this.type = type; }
}
