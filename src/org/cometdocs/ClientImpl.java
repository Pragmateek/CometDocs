package org.cometdocs;

// import java.nio.charset.Charset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.*;

class ClientImpl implements Client
{
    private static final String APIRoot = "https://www.cometdocs.com/api/v1/";

    private final DefaultHttpClient httpClient = new DefaultHttpClient();

    private Gson gson = null;
    
    public ClientImpl()
    {
    	GsonBuilder builder = new GsonBuilder();
  	    builder.registerTypeAdapter(Status.class, new StatusTypeAdapter());
  	    builder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
  	    builder.registerTypeAdapter(ConversionType.class, new ConversionTypeAdapter());
  	    builder.registerTypeAdapter(ConversionStatus.class, new ConversionStatusTypeAdapter());
  	    gson = builder.create();
    }
    
    private void checkAndThrow(Response response) throws Exception
    {
        if (response.getStatus() != Status.OK)
        {
        	if (response.getStatus() == Status.InvalidToken)
        	{
        		throw new InvalidTokenException(response.getMessage(), response.getStatus(), response.getError());
        	}
        	
            throw new CometDocsException(response.getMessage(), response.getStatus(), response.getError());
        }
    }

    private byte[] getBytes(InputStream stream) throws Exception
    {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = stream.read(data, 0, data.length)) != -1)
		{
		  buffer.write(data, 0, nRead);
		}
		
		return buffer.toByteArray();
    }
    
    public AuthenticationToken authenticate(String username, String password, String key) throws Exception
    {
    	return authenticate(username, password, key, null);
    }
    
    public AuthenticationToken authenticate(String username, String password, String key, Integer validity) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("clientKey", key));
    	if (validity != null)
    	{
    		params.add(new BasicNameValuePair("validity", validity.toString()));
    	}
    	    	
    	HttpPost post = new HttpPost(APIRoot + "authenticate");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
        AuthenticateResponse response = gson.fromJson(json, AuthenticateResponse.class);

        checkAndThrow(response);

        return new AuthenticationToken(response.getToken());
    }
    
    public FileInfo convertFile(AuthenticationToken token, FileInfo file, ConversionType conversion) throws Exception
    {
    	return convertFile(token, file, conversion, null);
    }
    
    public FileInfo convertFile(AuthenticationToken token, FileInfo file, ConversionType conversionType, Integer timeout) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("token", token.getValue()));
        params.add(new BasicNameValuePair("id", String.valueOf(file.getId())));
        params.add(new BasicNameValuePair("conversionType", conversionType.toString()));

        long nowTimestamp = new Date().getTime() / 1000;
        
        HttpPost post = new HttpPost(APIRoot + "convertFile");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
        Response response = gson.fromJson(json, Response.class);

        checkAndThrow(response);

        if (timeout == null) timeout = 1 * 60; // 1 minute by default
        Calendar timeoutCalendar = Calendar.getInstance();
        timeoutCalendar.add(Calendar.SECOND, timeout);
        Date timeoutTime = timeoutCalendar.getTime();
        
        while (Calendar.getInstance().getTime().before(timeoutTime))
        {
            Thread.sleep(5000);

            ConversionStatus status = getConversionStatus(token, file, conversionType);

            if (status == ConversionStatus.Started) continue;
            
            if (status == ConversionStatus.Succeeded)
            {
            	Conversion[] conversions = getConversions(token, file);
            	
            	Conversion match = null;
            			
            	for (Conversion conversion : conversions)
            	{
            		if (conversion.getType().equals(conversionType))
            		{
            			match = conversion;
            			break;
            		}
            	}
            	
            	if (match == null) throw new Exception("Conversion reported as successful but cannot find the conversion in conversions list!");
            	
            	FileInfo result = new FileInfo();
            	result.setId(match.getId());
            	
            	return result;
            }
            
            /* Notification[] notifications = getNotifications(token);

            Notification match = null;
            
            for (Notification n : notifications)
        	{
        		if ((n.getType() == NotificationType.ConversionDone || n.getType() == NotificationType.ConversionFailed) && n.getTimestamp() >= nowTimestamp)
        		{
        			match = n;
        		}
        	}

            if (match != null)
            {
                if (match.getType() == NotificationType.ConversionDone)
                {
                    return match.getFile();
                }
                else
                {
                    throw new Exception(String.format("Conversion of file '%s' (%d) failed!", file.getName(), file.getId()));
                }
            }*/
        }

        throw new Exception(String.format("Conversion of file '%s' (%d) timeouted: either it's still running or notification has been lost!", file.getName(), file.getId()));
    }

    public void createAccount(String name, String email, String password) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        
        HttpPost post = new HttpPost(APIRoot + "createAccount");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    Response response = gson.fromJson(json, Response.class);

        checkAndThrow(response);
    }
    
    public FolderInfo createFolder(AuthenticationToken token, FolderInfo parent, String name) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("token", token.getValue()));
        params.add(new BasicNameValuePair("folderId", String.valueOf(parent.getID())));
        params.add(new BasicNameValuePair("name", name));
        
        HttpPost post = new HttpPost(APIRoot + "createFolder");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    CreateFolderResponse response = gson.fromJson(json, CreateFolderResponse.class);

        checkAndThrow(response);

        return response.getFolder();
    }
    
    public void deleteFile(AuthenticationToken token, FileInfo file) throws Exception
    {
    	deleteFile(token, file, null);
    }
    
    public void deleteFile(AuthenticationToken token, FileInfo file, Boolean deleteRevisions) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("token", token.getValue()));
        params.add(new BasicNameValuePair("id", String.valueOf(file.getId())));
        if (deleteRevisions != null)
        {
            params.add(new BasicNameValuePair("deleteRevisions", deleteRevisions ? "1" : "0"));
        }

    	HttpPost post = new HttpPost(APIRoot + "deleteFile");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
        Response response = gson.fromJson(json, Response.class);

        checkAndThrow(response);
    }
    
    public void deleteFolder(AuthenticationToken token, FolderInfo folder) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("token", token.getValue()));
        params.add(new BasicNameValuePair("id", String.valueOf(folder.getID())));
    	    	
    	HttpPost post = new HttpPost(APIRoot + "deleteFolder");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    Response response = gson.fromJson(json, Response.class);

        checkAndThrow(response);
    }

    public File downloadFile(AuthenticationToken token, FileInfo file) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("token", token.getValue()));
        params.add(new BasicNameValuePair("id", String.valueOf(file.getId())));
    	    	
    	HttpPost post = new HttpPost(APIRoot + "downloadFile");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    if (httpResponse.getFirstHeader("content-type").getValue() == "text/json")
  	    {
  	    	String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    	Response response = gson.fromJson(json, Response.class);

  	    	checkAndThrow(response);
  	    	
  	    	throw new Exception(String.format("Response is json but it seems to be valid: '%s'!", json));
  	    }

  	    byte[] fileContent = getBytes(httpResponse.getEntity().getContent());
  	    
        File fileWithContent = new File();
        fileWithContent.setId(file.getId());
        fileWithContent.setName(file.getName());
        fileWithContent.setExtension(file.getExtension());
        fileWithContent.setSize(file.getSize());
        fileWithContent.hasConversions(file.hasConversions());
        fileWithContent.setContent(fileContent);
        
        return fileWithContent;
    }
    
    public Notification[] getNotifications(AuthenticationToken token) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("token", token.getValue()));
        
        HttpPost post = new HttpPost(APIRoot + "getNotifications");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
        GetNotificationsResponse response = gson.fromJson(json, GetNotificationsResponse.class);

        checkAndThrow(response);

        return response.getNotifications();
    }
    
    public void invalidateToken(AuthenticationToken token) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("token", token.getValue()));

    	HttpPost post = new HttpPost(APIRoot + "invalidateToken");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
        Response response = gson.fromJson(json, Response.class);

        checkAndThrow(response);
    }
    
    public Category[] getCategories() throws Exception
    {
    	HttpPost post = new HttpPost(APIRoot + "getCategories");
    	HttpResponse httpResponse = httpClient.execute(post);

        String json = EntityUtils.toString(httpResponse.getEntity());

        GetCategoriesResponse response = gson.fromJson(json, GetCategoriesResponse.class);

        checkAndThrow(response);

        return response.getCategories();
    }
    
    public Conversion[] getConversions(AuthenticationToken token) throws Exception
    {
    	return getConversions(token, null);
    }
    
    public Conversion[] getConversions(AuthenticationToken token, FileInfo file) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("token", token.getValue()));
        if (file != null) params.add(new BasicNameValuePair("file", String.valueOf(file.getId())));
    	    	
    	HttpPost post = new HttpPost(APIRoot + "getConversions");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);
  	    String json = EntityUtils.toString(httpResponse.getEntity());	    
  	    GetConversionsResponse response = gson.fromJson(json, GetConversionsResponse.class);

    	checkAndThrow(response);

    	return response.getConversions();
    }
    
    public ConversionStatus getConversionStatus(AuthenticationToken token, FileInfo file, ConversionType conversion) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("token", token.getValue()));
        params.add(new BasicNameValuePair("fileId", String.valueOf(file.getId())));
        params.add(new BasicNameValuePair("conversionType", conversion.toString()));
    	    	
    	HttpPost post = new HttpPost(APIRoot + "getConversionStatus");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);
  	    String json = EntityUtils.toString(httpResponse.getEntity());	    
  	    GetConversionStatusResponse response = gson.fromJson(json, GetConversionStatusResponse.class);

    	checkAndThrow(response);

    	return response.getConversionStatus();
    }
    
    public ConversionType[] getConversionTypes() throws Exception
    {    	
    	HttpPost post = new HttpPost(APIRoot + "getConversionTypes");
    	HttpResponse httpResponse = httpClient.execute(post);

        String json = EntityUtils.toString(httpResponse.getEntity());

        GetConversionTypesResponse response = gson.fromJson(json, GetConversionTypesResponse.class);

        checkAndThrow(response);

        return response.getConversionTypes();
    }
    
    public Folder getFolder(AuthenticationToken token) throws Exception
    {
    	return getFolder(token, null, null);
    }
    
    public Folder getFolder(AuthenticationToken token, FolderInfo folder) throws Exception
    {
    	return getFolder(token, folder, null);
    }
    
    public Folder getFolder(AuthenticationToken token, Boolean recursive) throws Exception
    {
    	return getFolder(token, null, recursive);
    }
    
    public Folder getFolder(AuthenticationToken token, FolderInfo folder, Boolean recursive) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("token", token.getValue()));
        if (folder != null) params.add(new BasicNameValuePair("folderId", String.valueOf(folder.getID())));
        if (recursive != null) params.add(new BasicNameValuePair("recursive", recursive ? "1" : "0"));
    	    	
    	HttpPost post = new HttpPost(APIRoot + "getFolder");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    GetFolderResponse response = gson.fromJson(json, GetFolderResponse.class);

        checkAndThrow(response);

        return response.getFolder();
    }
    
    public String[] getMethods() throws Exception
    {
    	HttpPost post = new HttpPost(APIRoot + "getMethods");    	
  	    HttpResponse httpResponse = httpClient.execute(post);
    	
  	    String json = EntityUtils.toString(httpResponse.getEntity());
	    
  	    GetMethodsResponse response = gson.fromJson(json, GetMethodsResponse.class);

      	checkAndThrow(response);

        return response.getMethods();
    }
    
    public FileInfo[] getPublicFiles(AuthenticationToken token) throws Exception
    {
    	return getPublicFiles(token, null);
    }
    
    public FileInfo[] getPublicFiles(AuthenticationToken token, Category category) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("token", token.getValue()));
        if (category != null) params.add(new BasicNameValuePair("categoryId", String.valueOf(category.getId())));
    	    	
    	HttpPost post = new HttpPost(APIRoot + "getPublicFiles");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    GetPublicFilesResponse response = gson.fromJson(json, GetPublicFilesResponse.class);

        checkAndThrow(response);

        return response.getFiles();
    }
    
    public FileInfo[] getSharedFiles(AuthenticationToken token) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("token", token.getValue()));
    	    	
    	HttpPost post = new HttpPost(APIRoot + "getSharedFiles");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    GetPublicFilesResponse response = gson.fromJson(json, GetPublicFilesResponse.class);

        checkAndThrow(response);

        return response.getFiles();
    }
    
    public void refreshToken(AuthenticationToken token) throws Exception
    {
    	refreshToken(token, null);
    }
    
    public void refreshToken(AuthenticationToken token, Integer validity) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("token", token.getValue()));
        if (validity != null)
        {
            params.add(new BasicNameValuePair("validity", validity.toString()));
        }

    	HttpPost post = new HttpPost(APIRoot + "refreshToken");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
        Response response = gson.fromJson(json, Response.class);

        checkAndThrow(response);
    }
    
    public void sendFile(AuthenticationToken token, FileInfo file, String[] recipients) throws Exception
    {
    	sendFile(token, file, recipients, null, null);
    }
    
    public void sendFile(AuthenticationToken token, FileInfo file, String[] recipients, String message) throws Exception
    {
    	sendFile(token, file, recipients, null, message);
    }
    
    public void sendFile(AuthenticationToken token, FileInfo file, String[] recipients, String sender, String message) throws Exception
    {
    	String recipientsString = "";
        for (String recipient : recipients)
        {
        	recipientsString += recipient + ",";
        }
        recipientsString = recipientsString.substring(0, recipientsString.length() - 1);
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        params.add(new BasicNameValuePair("token", token.getValue()));
        params.add(new BasicNameValuePair("id", String.valueOf(file.getId())));
        params.add(new BasicNameValuePair("recipients", recipientsString));
        if (sender != null)
        {
            params.add(new BasicNameValuePair("sender", sender));
        }
        if (message != null)
        {
            params.add(new BasicNameValuePair("message", message));
        }

        HttpPost post = new HttpPost(APIRoot + "sendFile");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    Response response = gson.fromJson(json, Response.class);

        checkAndThrow(response);
    }
    
    public FileInfo uploadFile(AuthenticationToken token, InputStream file, String name) throws Exception
    {
    	return uploadFile(token, file, name, null);
    }
    
    public FileInfo uploadFile(AuthenticationToken token, InputStream file, String name, Long folderId) throws Exception
    {
		byte[] fileContent = getBytes(file);

        HttpPost post = new HttpPost(APIRoot + "uploadFile");
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("token", new StringBody(token.getValue()));
        if (folderId != null)
        {
        	entity.addPart("folderId", new StringBody(folderId.toString()));	
        }
        entity.addPart("file", new ByteArrayBody(fileContent, name));
        post.setEntity(entity);
        
        HttpResponse httpResponse = httpClient.execute(post);
        String json = EntityUtils.toString(httpResponse.getEntity());

        UploadFileResponse response = gson.fromJson(json, UploadFileResponse.class);

        checkAndThrow(response);

        return response.getFile();
    }

    public FileInfo uploadFile(AuthenticationToken token, String file) throws Exception
    {
    	return uploadFile(token, file, null);
    }
    
    public FileInfo uploadFile(AuthenticationToken token, String file, Long folderId) throws Exception
    {
        String name = new File(file).getName();

        InputStream stream = new FileInputStream(file);
        
        return uploadFile(token, stream, name, folderId);
    }

    public FileInfo uploadFile(AuthenticationToken token, File file) throws Exception
    {
    	return uploadFile(token, file, (Long)null);
    }
    
    public FileInfo uploadFile(AuthenticationToken token, File file, Long folderId) throws Exception
    {
        InputStream stream = new ByteArrayInputStream(file.getContent());
        
        return uploadFile(token, stream, file.getName() + "." + file.getExtension(), folderId);
    }
    
    public FileInfo uploadFile(AuthenticationToken token, File file, FolderInfo folder) throws Exception
    {
        return uploadFile(token, file, folder.getID());
    }
    
    public FileInfo uploadFileFromUrl(AuthenticationToken token, String url) throws Exception
    {
    	return uploadFileFromUrl(token, url, null, null);
    }
    
    public FileInfo uploadFileFromUrl(AuthenticationToken token, String url, String name) throws Exception
    {
    	return uploadFileFromUrl(token, url, name, null);
    }
    
    public FileInfo uploadFileFromUrl(AuthenticationToken token, String url, Long folderId) throws Exception
    {
    	return uploadFileFromUrl(token, url, null, folderId);
    }    
    
    public FileInfo uploadFileFromUrl(AuthenticationToken token, String url, FolderInfo folder) throws Exception
    {
        return uploadFileFromUrl(token, url, null, folder.getID());
    }
    
    public FileInfo uploadFileFromUrl(AuthenticationToken token, String url, String name, Long folderId) throws Exception
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("token", token.getValue()));
        params.add(new BasicNameValuePair("url", url));
        if (name != null) params.add(new BasicNameValuePair("name", name));
        if (folderId != null) params.add(new BasicNameValuePair("folderId", folderId.toString()));
    	    	
    	HttpPost post = new HttpPost(APIRoot + "uploadFileFromUrl");
    	post.setEntity(new UrlEncodedFormEntity(params));
    	
  	    HttpResponse httpResponse = httpClient.execute(post);

  	    String json = EntityUtils.toString(httpResponse.getEntity());
  	    
  	    UploadFileFromUrlResponse response = gson.fromJson(json, UploadFileFromUrlResponse.class);

        checkAndThrow(response);

        return response.getFile();
    }
}
