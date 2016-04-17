package de.hm.edu.carads;


import java.util.Collection;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.AdvertiserController;
import de.hm.edu.carads.controller.AdvertiserControllerImpl;
import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;

@Path("advertisers")
public class AdvertiserRecource {
	private Gson gson = new Gson();
	private AdvertiserController ac = new AdvertiserControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_PROD));
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdvertiser() {
		
		Collection<Advertiser> advertiser;
		
		advertiser = ac.getAllEntities();
		
		if(advertiser.isEmpty())
			return Response.noContent().build();
		else
			return Response.ok(gson.toJson(advertiser)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAdvertiser(String input) {
		Advertiser adv = gson.fromJson(input, Advertiser.class);
		
		try{
			Advertiser advertiser = ac.addEntity(adv);
			return Response.ok(gson.toJson(advertiser)).build();
		}
		catch(AlreadyExistsException e){
			throw new WebApplicationException(409);
		}
		catch(InvalidAttributesException e){
			throw new WebApplicationException(400);
		}
		catch(Exception e){
			throw new WebApplicationException(500);
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdvertiser(@PathParam("id") String id) {
		try{
			Advertiser advertiser = ac.getEntity(id);
			return Response.ok(gson.toJson(advertiser)).build();
			
		}catch(NoContentException e){
			throw new WebApplicationException(404);
		}catch(Exception e){
			throw new WebApplicationException(500);
		}
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeAdvertiser(@PathParam("id") String id, String input){
		Advertiser adv = gson.fromJson(input, Advertiser.class);
	
		if(adv == null)
			throw new WebApplicationException(400);
		
		try{
			Advertiser changedAdvertiser = ac.changeEntity(id, adv);
			return Response.ok(gson.toJson(changedAdvertiser)).build();
		}
		catch(InvalidAttributesException e){
			throw new WebApplicationException(400);
		}catch(NoContentException e){
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}		
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteAdvertiser(@PathParam("id") String id){
		try {
			ac.deleteEntity(id);
			return Response.ok().build();
		} catch(NoContentException e){
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}
}
