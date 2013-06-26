package org.cometdocs;

public class Folder extends FolderInfo
{
    private Folder[] folders;
    public Folder[] getFolders() { return folders; }
	public void setFolders(Folder[] folders) { this.folders = folders; }

	private FileInfo[] files;
	public FileInfo[] getFiles() { return files; }
	public void setFiles(FileInfo[] files) { this.files = files; }
}