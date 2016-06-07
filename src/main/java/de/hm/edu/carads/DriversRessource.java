package de.hm.edu.carads;

import java.security.Principal;
import java.util.Collection;

import javax.naming.directory.InvalidAttributesException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.ApplicationController;
import de.hm.edu.carads.controller.ApplicationControllerImpl;
import de.hm.edu.carads.controller.RealmController;
import de.hm.edu.carads.controller.RealmControllerImpl;
import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.exceptions.HasConstraintException;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.transaction.DriverRegistration;
import de.hm.edu.carads.transaction.OfferInformation;
import de.hm.edu.carads.transaction.OfferResponse;

@Path("drivers")
public class DriversRessource {

	@Context
	private HttpServletRequest httpServletRequest;
	private Gson gson = new Gson();
	private DatabaseController dbController = new DatabaseControllerImpl(
			DatabaseFactory.INST_PROD);
	
	private ApplicationController modelController = new ApplicationControllerImpl(dbController);

	RealmController rc = new RealmControllerImpl(dbController);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDrivers() {
		Collection<Driver> drivers = modelController.getAllDrivers();
		if (drivers.isEmpty()) {
			return Response.noContent().build();
		} else {
			return Response.ok(gson.toJson(drivers)).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDriver(String input) {
		try {
			DriverRegistration newDriver = gson.fromJson(input, DriverRegistration.class);

			Driver driver = modelController.addDriver(newDriver);
			try{
				User realm = new User(newDriver.getEmail(), newDriver.getPassword(), "driver", driver.getId());
				rc.addUser(realm);
			}catch(NullPointerException e){
				//Password konnte nicht gelesen werden / Wurde nicht angegeben.
				//Fahrer wird wieder geloescht.
				System.err.println("Password missing");
				modelController.deleteDriver(driver.getId());
				throw new InvalidAttributesException();
			}
			
			return Response.ok(gson.toJson(driver)).build();

		} catch (AlreadyExistsException e) {
			throw new WebApplicationException(409);
		} catch (InvalidAttributesException e) {
			throw new WebApplicationException(400);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDriver(@PathParam("id") String id) {
		try {
			Driver driver = modelController.getDriver(id);
			return Response.ok(gson.toJson(driver)).build();

		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeDriver(@PathParam("id") String id, String input) {
		try {
			Driver currentDriver = getCurrentDriver();
			Driver driverData = gson.fromJson(input, Driver.class);

			if (driverData == null) {
				throw new InvalidAttributesException();
			}

			boolean isOwnProfile = currentDriver.getId().equals(
					driverData.getId());

			if (!isOwnProfile) {
				throw new WebApplicationException(401);
			}

			modelController.updateDriver(id, driverData);
			
			//Wurde die Email geaendert? Wenn ja muss auch Realm geaendert werden
			if(!currentDriver.getEmail().equals(driverData.getEmail())){
				rc.changeUsername(id, driverData.getEmail());
			}
			return Response.ok().build();
		} catch (AlreadyExistsException e) {
			throw new WebApplicationException(409);
		} catch (InvalidAttributesException e) {
			throw new WebApplicationException(400);
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (NullPointerException e){
			throw new WebApplicationException(401);
		} catch (Exception e) {
			System.out.println("woo");
			throw new WebApplicationException(500);
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteDriver(@PathParam("id") String id) {
		try {
			modelController.deleteDriver(id);
			return Response.ok().build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (HasConstraintException e){
			throw new WebApplicationException(406);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/{id}/cars")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDriverCars(@PathParam("id") String driverid) {
		try {
			Collection<Car> cars;
			cars = modelController.getCars(driverid);
			if (cars.isEmpty()) {
				return Response.noContent().build();
			}
			return Response.ok(gson.toJson(cars)).build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@POST
	@Path("/{id}/cars")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCar(@PathParam("id") String driverid, String input) {

		Car car;
		car = gson.fromJson(input, Car.class);
		if (car == null) {
			throw new WebApplicationException(400);
		}

		try {
			Car registredCar = modelController.addCar(driverid, car);
			return Response.ok(gson.toJson(registredCar)).build();
		} catch (InvalidAttributesException e) {
			throw new WebApplicationException(400);
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/{id}/cars/{car}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCar(@PathParam("id") String driverId,
			@PathParam("car") String carId) {
		try {
			Car car = modelController.getCar(driverId, carId);
			return Response.ok(gson.toJson(car)).build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@DELETE
	@Path("/{id}/cars/{car}")
	public Response deleteDriverCar(@PathParam("id") String driverId,
			@PathParam("car") String carId) {
		try {
			modelController.deleteCar(driverId, carId);
			return Response.ok().build();
		} catch(InvalidAttributesException e){
			throw new WebApplicationException(400);
		} catch(HasConstraintException e){
			throw new WebApplicationException(406);
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@PUT
	@Path("/{id}/cars/{car}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDriverCar(@PathParam("id") String driverId,
			@PathParam("car") String carId, String input) {
		Car car = gson.fromJson(input, Car.class);
		if (car == null) {
			throw new WebApplicationException(400);
		}

		try {
			Car newCar = modelController.updateCar(driverId, carId, car);
			return Response.ok(gson.toJson(newCar)).build();
		} catch (NoContentException e) {
			e.printStackTrace();
			throw new WebApplicationException(404);
		} catch (InvalidAttributesException e) {
			throw new WebApplicationException(400);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/{id}/requests")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCarRequests(@PathParam("id") String driverId) {
		try {
			Collection<OfferInformation> offerInfo = modelController
					.getOfferInformation(driverId);
			if (offerInfo.isEmpty())
				return Response.noContent().build();
			return Response.ok(gson.toJson(offerInfo)).build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@PUT
	@Path("/{id}/response")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response respond(@PathParam("id") String driverId, String input) {
		OfferResponse response = gson.fromJson(input, OfferResponse.class);
		//System.out.println(response.getResponse());
		try {
			modelController.respondToOffer(response.getCarId(),
					response.getAdvertiserId(), response.getCampaignId(),
					response.getResponse());
			return Response.ok().build();
		} catch (InvalidAttributesException e){
			throw new WebApplicationException(400);
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}
	
	

	private Driver getCurrentDriver() throws Exception {
		Principal principal = httpServletRequest.getUserPrincipal();
		String driverMail = principal.getName();
		Driver driver = modelController.getDriverByMail(driverMail);
		return driver;
	}
}
