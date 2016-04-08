package de.hm.edu.carads;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.ImageController;
import de.hm.edu.carads.controller.ImageControllerImpl;
import de.hm.edu.carads.models.Image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Path("images")
public class ImageRescource {

	private Gson gson = new Gson();
	private ImageController ic = new ImageControllerImpl();
	
	
	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadPdfFile(  @FormDataParam("file") InputStream fileInputStream,
	                                @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception
	{
	    String dataType = fileMetaData.getFileName().split("\\.")[1];
	    Image image;
	    try{
	    	image = ic.saveImage(fileInputStream, dataType);  
	    	return Response.status(201).entity(gson.toJson(image)).build();
	    	
	    }catch(IllegalArgumentException e){
	    	throw new WebApplicationException(406);
	    }catch(IOException e){
	    	throw new WebApplicationException(500);
	    }
	      
	}
	
	@GET
	@Path("/{id}")
	@Produces({ "image/png", "image/jpg" })
	public Response get(@PathParam("id") String id){
		
		try{
			byte[] imageData = ic.getImage(id);
		    return Response.ok(imageData).build();
		}
		catch(FileNotFoundException e){
			throw new WebApplicationException(404);
		}
		catch(Exception e){
			throw new WebApplicationException(500);
		}
		
		
	}
}
