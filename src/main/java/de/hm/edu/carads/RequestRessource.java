package de.hm.edu.carads;

import java.util.Collection;

import javax.ws.rs.GET;
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
import de.hm.edu.carads.controller.DriverController;
import de.hm.edu.carads.controller.DriverControllerImpl;
import de.hm.edu.carads.controller.RequestController;
import de.hm.edu.carads.controller.RequestControllerImpl;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.util.OfferInformation;

@Path("requests")
public class RequestRessource {

	private Gson gson = new Gson();
	private RequestController rc = new RequestControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_PROD));
	
	
	@GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarRequests(@PathParam("id") String driverId) {
        try {
            Collection<OfferInformation> offerInfo = rc.getOfferInformation(driverId);
            if(offerInfo.isEmpty())
            	return Response.noContent().build();
            return Response.ok(gson.toJson(offerInfo)).build();
        } catch (NoContentException e) {
            throw new WebApplicationException(404);
        } catch (Exception e) {
            throw new WebApplicationException(500);
        }
    }

}
