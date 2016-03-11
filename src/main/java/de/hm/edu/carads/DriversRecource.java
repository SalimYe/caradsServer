package de.hm.edu.carads;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;

import de.hm.edu.carads.controller.DriverController;
import de.hm.edu.carads.controller.impl.DriverControllerImpl;
import de.hm.edu.carads.models.Driver;

@Path("drivers")
public class DriversRecource {
	private Gson gson = new Gson();
	private DriverController dc = new DriverControllerImpl();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getDriver() {

		return gson.toJson(dc.getDrivers());

	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDrivers(@PathParam("id") String id) {

		return gson.toJson(dc.getDriver(id));

	}
	
}
