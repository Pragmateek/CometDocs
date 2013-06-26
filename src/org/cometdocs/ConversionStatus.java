package org.cometdocs;

public enum ConversionStatus
{
	NotStarted,
	Started,
	Failed,
	TimedOut,
	Succeeded,
	RequiresOCR,
	ServerTooBusyToProcessConversion
}
