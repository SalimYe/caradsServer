package de.hm.edu.carads;


import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.xml.bind.JAXBElement;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.DriverController;
import de.hm.edu.carads.controller.DriverControllerImpl;
import de.hm.edu.carads.controller.EntityValidator;
import de.hm.edu.carads.models.Car;
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
		
		if(dc.existDriverByEmail(driver.getEmail()))
			throw new WebApplicationException(409);
		
		try{
			Driver registredDriver = dc.addDriver(driver);
			return  Response.ok(gson.toJson(registredDriver)).build();
		}catch(NoContentException e){
			throw new WebApplicationException(404);
		}
		
		
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDrivers(@PathParam("id") String id) {
		try{
			
			Driver driver = dc.getDriver(id);
			return Response.ok(gson.toJson(driver)).build();
			
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
	public Response changeDriver(@PathParam("id") String id, String input){
		Driver driverData = gson.fromJson(input, Driver.class);
		
		if(driverData == null || !EntityValidator.isNewDriverValid(driverData)){
			throw new WebApplicationException(400);
		}
		
		try{
			Driver changedDriver = dc.changeDriver(id, driverData);
			return Response.ok(gson.toJson(changedDriver)).build();
		}catch(NoContentException e){
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
				
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
		
		
		Car car;
		
		try {
			car = dc.getCar(driverid);
			if(car == null){
				return Response.noContent().build();
			}
			return Response.ok(gson.toJson(car)).build();
			
			
		} catch(NoContentException e){
			throw new WebApplicationException(404);
		}
		catch (Exception e) {
			throw new WebApplicationException(500);
		}
		
	}
	
	@POST
	@Path("/{id}/cars")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDriverCar(@PathParam("id") String driverid, String input) {
		Driver driver;
		Car car;
		try {
			driver = dc.getDriver(driverid);
			car = gson.fromJson(input, Car.class);
		} catch (Exception e) {
			throw new WebApplicationException(404);
		}
		
		
			
		
				
		if(car == null || !EntityValidator.isNewCarValid(car)){
			throw new WebApplicationException(400);
		}
		try{
			Car registredCar = dc.addCar(driverid, car);
			return  Response.ok(gson.toJson(registredCar)).build();
		}catch(NoContentException e){
			throw new WebApplicationException(404);
		}
	}
}
