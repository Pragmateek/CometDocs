package org.cometdocs;

public enum NotificationType
{
	ConversionDone(1),
    OCRRequired(2),
    ConversionFailed(3),
    SharingRequestSent(4),
    FileShared(5);
    
	private int code;
    NotificationType(int code)
	{
		this.code = code;
	}
    
    public int getCode()
    {
    	return this.code;
    }
}
