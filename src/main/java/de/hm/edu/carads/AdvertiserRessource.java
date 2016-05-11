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
import de.hm.edu.carads.controller.DriverControllerImpl;
import de.hm.edu.carads.controller.RealmController;
import de.hm.edu.carads.controller.RealmControllerImpl;
import de.hm.edu.carads.controller.RequestController;
import de.hm.edu.carads.controller.RequestControllerImpl;
import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.models.comm.AdvertiserRegistration;
import de.hm.edu.carads.models.comm.Fellow;
import de.hm.edu.carads.models.util.Helper;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

@Path("advertisers")
public class AdvertiserRessource {

	@Context
	private HttpServletRequest httpServletRequest;
	private Gson gson = new Gson();
	DatabaseController dbController = new DatabaseControllerImpl(
			DatabaseFactory.INST_PROD);
	private AdvertiserController ac = new AdvertiserControllerImpl(dbController);
	private RequestController reqController = new RequestControllerImpl(
			new DriverControllerImpl(dbController), ac);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdvertiser() {

		Collection<Advertiser> advertiser;

		advertiser = ac.getAllEntities();

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
			AdvertiserRegistration advertiserRegistration = gson.fromJson(
					input, AdvertiserRegistration.class);
			RealmController rc = new RealmControllerImpl(
					new DatabaseControllerImpl(DatabaseFactory.INST_PROD));

			Advertiser advertiser = ac.addEntity(advertiserRegistration);
			
			User realm = new User(advertiser.getEmail(), Helper.getShaHash(advertiserRegistration.getPassword()), "advertiser",
					advertiser.getId());
			rc.addEntity(realm);

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
			Advertiser advertiser = ac.getEntity(id);
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

			if (adv == null) {
				throw new InvalidAttributesException();
			}
			Advertiser changedAdvertiser = ac.changeEntity(id, adv);
			return Response.ok(gson.toJson(changedAdvertiser)).build();
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
			ac.deleteEntity(id);
			return Response.ok().build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
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
			Campaign addedCampaign = ac.addCampaign(id, c);
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
			Collection<Campaign> campaigns = ac.getCampaigns(id);
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
			Campaign campaign = ac.getCampaign(id, cid);
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
			ac.deleteCampaign(id, cid);
			return Response.ok().build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (Exception e) {
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
			Campaign updatedCampaign = ac.updateCampaign(id, cid, c);
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
		Fellow f = gson.fromJson(input, Fellow.class);
		if (f == null || f.getCarId().isEmpty()) {
			throw new WebApplicationException(400);
		}

		try {
			Campaign campaign = ac.requestVehicleForCampaign(id, cid,
					f.getCarId());
			return Response.ok(gson.toJson(campaign)).build();
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
			Collection<Car> cars = reqController.getAvailableCars(id, cid);
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
		String driverMail = principal.getName();
		Advertiser advertiser = ac.getEntityByMail(driverMail);
		return advertiser;
	}
}
