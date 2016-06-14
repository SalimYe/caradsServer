package de.hm.edu.carads;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

/**
 * Die REST-Schnittstelle fuer die Bereitstellung aller benötigten Bild-Methoden
 * nach Außen.
 * @author BK
 *
 */
@Path("images")
public class ImageAPI {

	/**
	 * Dieses Objekt wird zum Parsen von JSON-Dateien in Java-Objekte verwendet.
	 * Zusaetzlich können Java-Objekte als JSON-Datein formatiert werden.
	 */
	private Gson gson = new Gson();
	
	/**
	 * Die Implementierung der Logik.
	 */
	private ImageController ic = new ImageControllerImpl();
	
	/**
	 * Ein Bild wird hochgeladen, wenn es als multipart/form-data ueber ein
	 * Formular an diese Methode geschickt wird.
	 * Der Rueckgabewert enthaelt die ID dieses Bildes auf dem Server.
	 * @param fileInputStream
	 * @param fileMetaData
	 * @return 201 wenn angelegt<br>
	 * 406 wenn fehlerhafte Datei hochgeladen wird<br>
	 * 500 Servererror
	 * @throws Exception
	 */
	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(  @FormDataParam("file") InputStream fileInputStream,
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
	    	System.out.println("Error while uploading");
	    	throw new WebApplicationException(500);
	    }
	}
	
	/**
	 * Ein Bild wird anhand einer ID vom Server geladen und angezeigt.
	 * 
	 * @param id
	 * @return 200 Bilddaten<br>
	 * 404 wenn ID nicht auf Server gefunden<br>
	 * 500 Servererror
	 */
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
	
	/**
	 * Ein Bild wird vom Filesystem des Servers geloescht.
	 * 
	 * @param id
	 * @return 200 wenn geloescht<br>
	 * 404 wenn ID nicht gefunden<br>
	 * 500 Servererror
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteImage(@PathParam("id") String id){
		try{
			ic.deleteImage(id);
			return Response.ok().build();
		}
		catch(FileNotFoundException e){
			throw new WebApplicationException(404);
		}
		catch(Exception e){
			throw new WebApplicationException(500);
		}
	}
}
