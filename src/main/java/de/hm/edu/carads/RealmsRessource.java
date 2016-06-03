package de.hm.edu.carads;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;

import de.hm.edu.carads.controller.ApplicationController;
import de.hm.edu.carads.controller.ApplicationControllerImpl;
import de.hm.edu.carads.controller.RealmController;
import de.hm.edu.carads.controller.RealmControllerImpl;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.User;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

@Path("realms")
public class RealmsRessource {
	
    @Context
    private HttpServletRequest httpServletRequest;
    RealmController rc = new RealmControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_PROD));

    private Gson gson = new Gson();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRealm() {
        
        Principal principal = httpServletRequest.getUserPrincipal();
        String username = principal.getName();
        User realm = rc.getRealmByUsername(username);
        realm.setCredentials(null);
        return Response.ok(gson.toJson(realm)).build();

    }
    
}
