package org.cometdocs;

import java.io.InputStream;

public interface Client
{
	public AuthenticationToken authenticate(String username, String password, String key) throws Exception;
	/**
	 * Authenticate to the CometDocs API: https://www.cometdocs.com/developer/apiDocumentation#method-authenticate
	 * 
	 * @param username The CometDocs website username (typically an email address).
	 * @param password The CometDocs website password.
	 * @param key The CometDocs API key (you can create one here: https://www.cometdocs.com/developer/myApps).
	 * @param validity The desired validity of the emitted token.
	 * @return An authentication token to use with subsequent API calls.
	 * @throws Exception
	 */
	public AuthenticationToken authenticate(String username, String password, String key, Integer validity) throws Exception;
	public FileInfo convertFile(AuthenticationToken token, FileInfo file, ConversionType conversion) throws Exception;
	public FileInfo convertFile(AuthenticationToken token, FileInfo file, ConversionType conversionType, Integer timeout) throws Exception;
	public void createAccount(String name, String email, String password) throws Exception;
	public FolderInfo createFolder(AuthenticationToken token, FolderInfo parent, String name) throws Exception;
	public void deleteFile(AuthenticationToken token, FileInfo file) throws Exception;
	public void deleteFile(AuthenticationToken token, FileInfo file, Boolean deleteRevisions) throws Exception;
	public void deleteFolder(AuthenticationToken token, FolderInfo folder) throws Exception;
	public File downloadFile(AuthenticationToken token, FileInfo file) throws Exception;
	public Notification[] getNotifications(AuthenticationToken token) throws Exception;
	public void invalidateToken(AuthenticationToken token) throws Exception;
	public Category[] getCategories() throws Exception;
	public Conversion[] getConversions(AuthenticationToken token) throws Exception;
	public Conversion[] getConversions(AuthenticationToken token, FileInfo file) throws Exception;
	public ConversionStatus getConversionStatus(AuthenticationToken token, FileInfo file, ConversionType conversion) throws Exception;
	public ConversionType[] getConversionTypes() throws Exception;
	public Folder getFolder(AuthenticationToken token) throws Exception;
	public Folder getFolder(AuthenticationToken token, FolderInfo folder) throws Exception;
	public Folder getFolder(AuthenticationToken token, Boolean recursive) throws Exception;
	public Folder getFolder(AuthenticationToken token, FolderInfo folder, Boolean recursive) throws Exception;
	public String[] getMethods() throws Exception;
	public FileInfo[] getPublicFiles(AuthenticationToken token) throws Exception;
	public FileInfo[] getPublicFiles(AuthenticationToken token, Category category) throws Exception;
	public FileInfo[] getSharedFiles(AuthenticationToken token) throws Exception;
	public void refreshToken(AuthenticationToken token) throws Exception;
	public void refreshToken(AuthenticationToken token, Integer validity) throws Exception;
	public void sendFile(AuthenticationToken token, FileInfo file, String[] recipients) throws Exception;
	public void sendFile(AuthenticationToken token, FileInfo file, String[] recipients, String message) throws Exception;
	public void sendFile(AuthenticationToken token, FileInfo file, String[] recipients, String sender, String message) throws Exception;
	public FileInfo uploadFile(AuthenticationToken token, InputStream file, String name) throws Exception;
	public FileInfo uploadFile(AuthenticationToken token, InputStream file, String name, Long folderId) throws Exception;
	public FileInfo uploadFile(AuthenticationToken token, String file) throws Exception;
	public FileInfo uploadFile(AuthenticationToken token, String file, Long folderId) throws Exception;
	public FileInfo uploadFile(AuthenticationToken token, File file) throws Exception;
	public FileInfo uploadFile(AuthenticationToken token, File file, Long folderId) throws Exception;
	public FileInfo uploadFile(AuthenticationToken token, File file, FolderInfo folder) throws Exception;
	public FileInfo uploadFileFromUrl(AuthenticationToken token, String url) throws Exception;
	public FileInfo uploadFileFromUrl(AuthenticationToken token, String url, String name) throws Exception;
	public FileInfo uploadFileFromUrl(AuthenticationToken token, String url, Long folderId) throws Exception;
	public FileInfo uploadFileFromUrl(AuthenticationToken token, String url, FolderInfo folder) throws Exception;
	public FileInfo uploadFileFromUrl(AuthenticationToken token, String url, String name, Long folderId) throws Exception;
}
