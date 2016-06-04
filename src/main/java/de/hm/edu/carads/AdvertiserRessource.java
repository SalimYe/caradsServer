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
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.models.comm.OfferRequest;
import de.hm.edu.carads.models.util.Hasher;
import de.hm.edu.carads.transaction.AdvertiserRegistration;
import de.hm.edu.carads.transaction.EnrichedCampaign;

@Path("advertisers")
public class AdvertiserRessource {

	@Context
	private HttpServletRequest httpServletRequest;
	private Gson gson = new Gson();
	private DatabaseController dbController = new DatabaseControllerImpl(DatabaseFactory.INST_PROD);
	private ApplicationController modelController = new ApplicationControllerImpl(dbController);
	private RealmController rc = new RealmControllerImpl(dbController);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdvertiser() {

		Collection<Advertiser> advertiser;

		advertiser = modelController.getAllAdvertisers();

		if (advertiser.isEmpty()) {
			return Response.noContent().build();
		} else {
			return Response.ok(gson.toJson(advertiser)).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAdvertiser(String input) {

		try {
			AdvertiserRegistration advertiserRegistration = gson.fromJson(input, AdvertiserRegistration.class);
			
			Advertiser advertiser = modelController.addAdvertiser(advertiserRegistration);
			
			try{
				User realm = new User(advertiserRegistration.getEmail(), advertiserRegistration.getPassword(), "advertiser", advertiser.getId());
				rc.addUser(realm);
			}catch (NullPointerException e){
				//Password konnte nicht gelesen werden / Wurde nicht angegeben.
				//Werbender wird wieder geloescht.
				modelController.deleteAdvertiser(advertiser.getId());
				throw new InvalidAttributesException();
			}
			
			return Response.ok(gson.toJson(advertiser)).build();

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
	public Response getAdvertiser(@PathParam("id") String id) {
		try {
			Advertiser advertiser = modelController.getAdvertiser(id);
			return Response.ok(gson.toJson(advertiser)).build();

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
	public Response changeAdvertiser(@PathParam("id") String id, String input) {
		try {
			Advertiser currentAdvertiser = getCurrentAdvertiser();
			Advertiser adv = gson.fromJson(input, Advertiser.class);

			boolean isOwnProfile = currentAdvertiser.getId()
					.equals(adv.getId());

			if (!isOwnProfile) {
				throw new WebApplicationException(401);
			}

			modelController.updateAdvertiser(id, adv);
			
			//Wurde die Email geaendert? Wenn ja muss auch Realm geaendert werden
			if(!currentAdvertiser.getEmail().equals(adv.getEmail())){
				rc.changeUsername(id, adv.getEmail());
			}
			
			return Response.ok().build();
		} catch (InvalidAttributesException e) {
			throw new WebApplicationException(400);
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteAdvertiser(@PathParam("id") String id) {
		try {
			modelController.deleteAdvertiser(id);
			return Response.ok().build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (HasConstraintException e){
			throw new WebApplicationException(406);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@POST
	@Path("/{id}/campaigns")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCampaign(@PathParam("id") String id, String input) {
		Campaign c = gson.fromJson(input, Campaign.class);
		if (c == null) {
			throw new WebApplicationException(400);
		}

		try {
			Campaign addedCampaign = modelController.addCampaign(id, c);
			return Response.ok(gson.toJson(addedCampaign)).build();
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/{id}/campaigns")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCampaigns(@PathParam("id") String id) {
		try {
			Collection<Campaign> campaigns = modelController.getCampaigns(id);
			if (campaigns.isEmpty()) {
				return Response.noContent().build();
			} else {
				return Response.ok(gson.toJson(campaigns)).build();
			}
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/{id}/campaigns/{cid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCampaign(@PathParam("id") String id,
			@PathParam("cid") String cid) {
		try {
			EnrichedCampaign campaign = modelController.getEnrichedCampaign(id, cid);
			return Response.ok(gson.toJson(campaign)).build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@DELETE
	@Path("/{id}/campaigns/{cid}")
	public Response deleteCampaign(@PathParam("id") String id,
			@PathParam("cid") String cid) {
		try {
			modelController.deleteCampaign(id, cid);
			return Response.ok().build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch(HasConstraintException e){
			throw new WebApplicationException(406);
		}catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@PUT
	@Path("/{id}/campaigns/{cid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCampaign(@PathParam("id") String id,
			@PathParam("cid") String cid, String input) {
		Campaign c = gson.fromJson(input, Campaign.class);
		if (c == null) {
			throw new WebApplicationException(401);
		}

		try {
			Campaign updatedCampaign = modelController.updateCampaign(id, cid, c);
			return Response.ok(gson.toJson(updatedCampaign)).build();
		} catch (InvalidAttributesException e) {
			throw new WebApplicationException(400);
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(403);
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@POST
	@Path("/{id}/campaigns/{cid}/cars")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCarToCampaign(@PathParam("id") String id,
			@PathParam("cid") String cid, String input) {
		OfferRequest[] fellows = gson.fromJson(input, OfferRequest[].class);
		if (fellows == null || fellows.length<1) {
			throw new WebApplicationException(400);
		}

		try {
			for(int i=0; i<fellows.length; i++){
				modelController.requestVehicleForCampaign(id, cid, fellows[i].getCarId());
			}
			return Response.ok().build();
		} catch (IllegalAccessException e) {
			throw new WebApplicationException(404);
		} catch (AlreadyExistsException e) {
			throw new WebApplicationException(409);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/{id}/campaigns/{cid}/availableCars")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvailableCars(@PathParam("id") String id,
			@PathParam("cid") String cid) {
		try {
			Collection<Car> cars = modelController.getAvailableCars(id, cid);
			if (cars.isEmpty())
				return Response.noContent().build();
			return Response.ok(gson.toJson(cars)).build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	private Advertiser getCurrentAdvertiser() throws Exception {
		Principal principal = httpServletRequest.getUserPrincipal();
		String adMail = principal.getName();
		Advertiser advertiser = modelController.getAdvertiserByMail(adMail);
		return advertiser;
	}
}
