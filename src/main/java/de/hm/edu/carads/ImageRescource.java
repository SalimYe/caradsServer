package de.hm.edu.carads;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.Gson;

import de.hm.edu.carads.models.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Path("images")
public class ImageRescource {

	private Gson gson = new Gson();
	private static final String UPLOAD_PATH = "/tmp/";
	
	@POST
	@Path("/upload")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadPdfFile(  @FormDataParam("file") InputStream fileInputStream,
	                                @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception
	{
	    String filename;
	    
	    try
	    {
	        int read = 0;
	        byte[] bytes = new byte[1024];
	        
	        String dataType = fileMetaData.getFileName().split("\\.")[1];
	        
	        if(!dataType.equals("jpg"))
	        	throw new WebApplicationException(406);
	        
	        filename = UPLOAD_PATH + UUID.randomUUID() + "." + dataType;
	        
	        OutputStream out = new FileOutputStream(new File(filename));
	        while ((read = fileInputStream.read(bytes)) != -1) 
	        {
	            out.write(bytes, 0, read);
	        }
	        out.flush();
	        out.close();
	    } catch (IOException e) 
	    {
	        throw new WebApplicationException(500);
	    }
	    
	    Image image = new Image(filename);
	    return Response.ok(gson.toJson(image)).build();
	}
	
	
	@GET
	@Path("/upload")
	public Response get(){
		return Response.status(200).build();
	}
}
