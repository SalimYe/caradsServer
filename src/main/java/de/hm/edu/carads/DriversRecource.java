package de.hm.edu.carads;


import java.util.List;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.xml.bind.JAXBElement;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.DriverController;
import de.hm.edu.carads.controller.DriverControllerImpl;
import de.hm.edu.carads.controller.EntityValidator;
import de.hm.edu.carads.models.Driver;

@Path("drivers")
public class DriversRecource {
	private Gson gson = new Gson();
	private DriverController dc = new DriverControllerImpl();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDriver(
			@DefaultValue("0") @QueryParam("startAt") int startAt,
			@DefaultValue("10") @QueryParam("length") int length) {
		
		Collection<Driver> drivers;
		
		drivers = dc.getDrivers(startAt, length);
		
		if(drivers.isEmpty())
			return Response.noContent().build();
		else
			return Response.ok(gson.toJson(drivers)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDriver(String input) {
		Driver driver = gson.fromJson(input, Driver.class);
		
		if(driver == null || !EntityValidator.isNewDriverValid(driver)){
			throw new WebApplicationException(400);
		}
		
		if(dc.existDriver(driver.getEmail()))
			throw new WebApplicationException(409);
		
		Driver registredDriver = dc.addDriver(driver);
		return  Response.ok(gson.toJson(registredDriver)).build();
		
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDrivers(@PathParam("id") String id) {
		Driver driver = dc.getDriver(id);
		if(driver == null)
			throw new WebApplicationException(404);
		return Response.ok(gson.toJson(driver)).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeDriver(String input){
		Driver driverChanges = gson.fromJson(input, Driver.class);
		if(driverChanges == null || !EntityValidator.isNewDriverValid(driverChanges)){
			throw new WebApplicationException(400);
		}
		Driver changedDriver = dc.changeDriver(driverChanges);
		
		if(changedDriver == null){
			throw new WebApplicationException(404);
		}
		System.out.println("put");
		return Response.ok(gson.toJson(changedDriver)).build();
	}
	
}
