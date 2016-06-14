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
import de.hm.edu.carads.transaction.AdvertiserRegistration;
import de.hm.edu.carads.transaction.EnrichedCampaign;
import de.hm.edu.carads.transaction.OfferRequest;

/**
 * Die REST-Schnittstelle fuer die Bereitstellung aller benötigten Werbenden-Methoden
 * nach Außen.
 * Parameter werden immer als JSON erwartet.
 * Rueckgabewerte sind ebenfalls immer JSON-Dateien.
 * @author BK
 *
 */
@Path("advertisers")
public class AdvertiserAPI {

	/**
	 * Dieses Objekt enthaelt Informationen aus dem Servlet.
	 */
	@Context
	private HttpServletRequest httpServletRequest;
	
	/**
	 * Dieses Objekt wird zum Parsen von JSON-Dateien in Java-Objekte verwendet.
	 * Zusaetzlich können Java-Objekte als JSON-Datein formatiert werden.
	 */
	private Gson gson = new Gson();
	
	/**
	 * Die Schnittstelle zur Datenbank.
	 */
	private DatabaseController dbController = new DatabaseControllerImpl(DatabaseFactory.INST_PROD);
	
	/**
	 * Die Schnittstelle zur Applikationslogik.
	 */
	private ApplicationController modelController = new ApplicationControllerImpl(dbController);
	
	/**
	 * Die Schnittstelle zur Authentifizierung.
	 */
	private RealmController rc = new RealmControllerImpl(dbController);
	
	/**
	 * Alle Werbenden werden angezeigt.
	 * 
	 * @return 200 Werbende als Collection<br>
	 * 204 keine Werbende vorhanden
	 */
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

	/**
	 * Ein Werbender wird hinzugefuegt. Er wird auch als User in der Realm-Collection
	 * gespeichert um sich spaeter einzuloggen.
	 * 
	 * @param Werbender als JSON-Objekt
	 * @return 200 angelegt<br>
	 * 400 Falsche angaben<br>
	 * 409 wenn E-Mail bereits vorhanden<br>
	 * 500 Servererror
	 */
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

	/**
	 * Ein Werbender wird anhand seiner ID zurueck gegeben.
	 * 
	 * @param id
	 * @return 200 Werbender als JSON<br>
	 * 404 nicht gefunden<br>
	 * 500 Servererror
	 */
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

	/**
	 * Daten eines Werbenden werden geandert.
	 * @param id
	 * @param input
	 * @return 200 wenn geandert<br>
	 * 400 wenn falsche Angaben<br>
	 * 401 wenn keine Berechtigung<br>
	 * 404 wenn Werbender nicht gefunden<br>
	 * 500 Servererror
	 */
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

	/**
	 * Ein Werbender wird anhand seiner ID geloescht.
	 * 
	 * @param id
	 * @return 200 wenn geloescht<br>
	 * 404 wenn nicht gefunden<br>
	 * 406 kann aufgrund Abhaengigkeiten nicht geloescht werden<br>
	 * 500 Servererrror
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteAdvertiser(@PathParam("id") String id) {
		try {
			modelController.deleteAdvertiser(id);
			rc.deleteUser(id);
			return Response.ok().build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (HasConstraintException e){
			throw new WebApplicationException(406);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	/**
	 * Eine Kampagne wird gespeichert.
	 * 
	 * @param id des Werbenden
	 * @param input Kampagne als JSON-Objekt
	 * @return 200 wenn angelegt<br>
	 * 400 falsche Angaben<br>
	 * 500 Servererror
	 */
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
		} catch(IllegalArgumentException e){
			throw new WebApplicationException(400);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	/**
	 * Kampagnen eines Werbenden werden angezeigt.
	 * 
	 * @param id des Werbenden
	 * @return 200 Kampagnen als JSON-Collection<br>
	 * 404 ID nicht gefunden<br>
	 * 500 Servererror
	 */
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

	/**
	 * Eine Kampagne wird angezeigt.
	 * 
	 * @param id ID des Werbender
	 * @param cid ID der Kampagne
	 * @return 200 Kampagne als JSON<br>
	 * 404 Werbender oder Kampagne nicht gefunden<br>
	 * 500 Servererrror
	 */
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

	/**
	 * Eine Kampagne wird geloescht.
	 * 
	 * @param id Werbender
	 * @param cid Kampagne
	 * @return 200 geloescht<br>
	 * 400 falsche Angaben<br>
	 * 404 Werbender oder Kampagne nicht gefunden<br>
	 * 406 kann aufgrund Abhaengigkeiten nicht geloescht werden<br>
	 * 500 Servererror
	 */
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
		} catch (InvalidAttributesException e) {
			throw new WebApplicationException(400);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	/**
	 * Kampagne wird geandert.
	 * Daten der Kampagne muessen komplett vorhanden sein.
	 * 
	 * @param id Werbender
	 * @param cid Kampagne 
	 * @param input Kampagnendaten als JSON
	 * @return 200 geandert<br>
	 * 400 falsche Angaben<br>
	 * 401 Keine Berechtigung<br>
	 * 403 Fehlangaben<br>
	 * 404 Werbender oder Kampagne nicht gefunden.
	 * 500 Servererrror
	 */
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

	/**
	 * Ein Auto wird fuer eine Kampagne angefragt.
	 * 
	 * @param id Werbender
	 * @param cid Kampagne
	 * @param input Anfrage als JSON-Collection
	 * @return 200 angefragt<br>
	 * 400 Falschangaben<br>
	 * 404 Werbender oder Kampagne nicht gefunden<br>
	 * 409 Das Fahrzeug wurde bereits angefragt<br>
	 * 500 Servererror
	 */
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

	/**
	 * Alle Fahrzeuge die fuer diese Kampagne angefragt werden koennen werden angezeigt.
	 * 
	 * @param id Werbender
	 * @param cid Kampagne
	 * @return 204 kein Fahrzeug verfuegbar<br>
	 * 200 verfuegbare Fahrzeuge<br>
	 * 404 Werbender oder Kampagne nicht gefunden<br>
	 * 500 Servererror
	 */
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

	/**
	 * Der aktuell angemeldete Werbende wird zurueck gegeben.
	 * 
	 * @return angemeldeter Werbende
	 * @throws Exception
	 */
	private Advertiser getCurrentAdvertiser() throws Exception {
		Principal principal = httpServletRequest.getUserPrincipal();
		String adMail = principal.getName();
		Advertiser advertiser = modelController.getAdvertiserByMail(adMail);
		return advertiser;
	}
}
