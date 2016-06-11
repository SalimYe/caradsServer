package de.hm.edu.carads;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.RealmController;
import de.hm.edu.carads.controller.RealmControllerImpl;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.transaction.Credidentials;

@Path("realms")
public class RealmsAPI {

	@Context
	private HttpServletRequest httpServletRequest;
	RealmController rc = new RealmControllerImpl(new DatabaseControllerImpl(
			DatabaseFactory.INST_PROD));

	private Gson gson = new Gson();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRealm() {

		try {
			Principal principal = httpServletRequest.getUserPrincipal();
			String username = principal.getName();
			User realm;
			realm = rc.getUser(username);
			realm.setPassword(null);
			return Response.ok(gson.toJson(realm)).build();
		} catch (NoSuchAlgorithmException e) {
			throw new WebApplicationException(503);
		} catch (NullPointerException e) {
			throw new WebApplicationException(401);
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/logout")
	public Response logout(@Context UriInfo uriInfo) throws ServletException{
		try {
			httpServletRequest.logout();
			URI uri = uriInfo.getBaseUriBuilder().path("../").build();
			return Response.seeOther(uri).build();
		} catch (ServletException e) {
			throw new WebApplicationException(500);
		}

	}

	@PUT
	@Path("/{id}/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@PathParam("id") String driverId,
			String input) {

		Credidentials credidentials = gson.fromJson(input, Credidentials.class);
		try {
			rc.changeCredentials(driverId, credidentials);
			return Response.ok().build();
		} catch (NoContentException e) {
			throw new WebApplicationException(404);
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(401);
		} catch(NoSuchAlgorithmException e){
			throw new WebApplicationException(406);
		}
		
		catch (Exception e) {
			throw new WebApplicationException(500);
		}

	}

}
