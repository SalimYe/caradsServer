package de.hm.edu.carads;


import java.util.Collection;

import javax.naming.directory.InvalidAttributesException;
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
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.DriverController;
import de.hm.edu.carads.controller.DriverController2;
import de.hm.edu.carads.controller.DriverControllerImpl;
import de.hm.edu.carads.controller.EntityController;
import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

@Path("drivers")
public class DriversRecource {
	private Gson gson = new Gson();
	private DriverController2 dc = new DriverController2(Driver.class, new DatabaseControllerImpl(DatabaseFactory.INST_PROD));
	//private DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_PROD));
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDriver(
			@DefaultValue("0") @QueryParam("startAt") int startAt,
			@DefaultValue("10") @QueryParam("length") int length) {
		Collection<Driver> drivers;
		drivers = dc.getAllEntities();
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
		try{
			Driver registredDriver = dc.addEntity(driver);
			return  Response.ok(gson.toJson(registredDriver)).build();
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
	public Response getDrivers(@PathParam("id") String id) {
		try{
			Driver driver = dc.getEntity(id);
			return Response.ok(gson.toJson(driver)).build();
			
		}catch(NoContentException e){
			throw new WebApplicationException(404);
		}catch(Exception e){
			throw new WebApplicationException(500);
		}
	}
	/*
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeDriver(@PathParam("id") String id, String input){
		Driver driverData = gson.fromJson(input, Driver.class);
		if(driverData == null)
			throw new WebApplicationException(400);
		
		try{
			Driver changedDriver = dc.changeDriver(id, driverData);
			return Response.ok(gson.toJson(changedDriver)).build();
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
	public Response deleteDriver(@PathParam("id") String id){
		try {
			dc.deleteDriver(id);
			return Response.ok().build();
		} catch(NoContentException e){
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}
	
	
	@GET
	@Path("/{id}/cars")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDriverCars(@PathParam("id") String driverid) {
		try {
			Car car;
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

		car = gson.fromJson(input, Car.class);
		if(car == null){
			throw new WebApplicationException(400);
		}
		
		try{
			Car registredCar = dc.addCar(driverid, car);
			return  Response.ok(gson.toJson(registredCar)).build();
		} catch (InvalidAttributesException e){
			throw new WebApplicationException(400);
		} catch(NoContentException e){
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}
	
	@DELETE
	@Path("/{id}/cars")
	public Response deleteDriverCar(@PathParam("id") String driverid){
		try {
			dc.deleteCar(driverid);
			return Response.ok().build();
		} catch(NoContentException e){
			throw new WebApplicationException(404);
		}
		catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}
	*/
}
