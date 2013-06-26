package org.cometdocs;

import com.google.gson.annotations.SerializedName;

public class Notification
{
	private NotificationType type;
    public NotificationType getType() { return type; }
	public void setType(NotificationType type) { this.type = type; }	
	
	@SerializedName("time")
    private int timestamp;
	public int getTimestamp() { return timestamp; }
	public void setTimestamp(int timestamp) { this.timestamp = timestamp; }
	
    private FileInfo file;
    public FileInfo getFile() { return file; }
	public void setFile(FileInfo file) { this.file = file; }
}
