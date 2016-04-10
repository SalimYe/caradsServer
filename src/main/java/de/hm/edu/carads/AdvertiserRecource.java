package de.hm.edu.carads;


import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.AdvertiserController;
import de.hm.edu.carads.controller.AdvertiserControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;

@Path("advertisers")
public class AdvertiserRecource {
	private Gson gson = new Gson();
	private AdvertiserController ac = new AdvertiserControllerImpl(DatabaseFactory.INST_PROD);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdvertiser() {
		
		Collection<Advertiser> advertiser;
		
		advertiser = ac.getAdvertiser();
		
		if(advertiser.isEmpty())
			return Response.noContent().build();
		else
			return Response.ok(gson.toJson(advertiser)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDriver(String input) {
		
		return  Response.ok().build();
		
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDrivers(@PathParam("id") String id) {
		
		return Response.ok().build();
	}
	/*
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeDriver(@PathParam("id") String id, String input){
		Driver driverData = gson.fromJson(input, Driver.class);
		
		if(driverData == null || !EntityValidator.isNewDriverValid(driverData)){
			throw new WebApplicationException(400);
		}

		Driver changedDriver = dc.changeDriver(id, driverData);
		
		if(changedDriver == null){
			throw new WebApplicationException(404);
		}
		return Response.ok(gson.toJson(changedDriver)).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteDriver(@PathParam("id") String id){
		
		if(!dc.deleteDriver(id))
			throw new WebApplicationException(404);
		
		return Response.ok().build();
	}
	
	
	@GET
	@Path("/{id}/cars")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDriverCars(@PathParam("id") String driverid) {
		
		if(dc.getDriver(driverid) == null)
			throw new WebApplicationException(404);
		
		Car car;
		
		car = dc.getCar(driverid);
		
		if(car == null)
			return Response.noContent().build();
		else
			return Response.ok(gson.toJson(car)).build();
	}
	
	@POST
	@Path("/{id}/cars")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDriverCar(@PathParam("id") String driverid, String input) {
		Driver driver = dc.getDriver(driverid);
		Car car = gson.fromJson(input, Car.class);
		
		if(driver == null)
			throw new WebApplicationException(404);
		
				
		if(car == null || !EntityValidator.isNewCarValid(car)){
			throw new WebApplicationException(400);
		}
		
		Car registredCar = dc.addCar(driverid, car);
		return  Response.ok(gson.toJson(registredCar)).build();
		
	}
	*/
}
