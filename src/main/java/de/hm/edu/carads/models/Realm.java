package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author florian
 */
public class Realm extends Model {

    private String username;
    private String credentials;
    private Collection <Role> roles;

    public Realm(String username, String credentials) {
        super();
        this.username = username;
        this.credentials = credentials;
        this.roles = new ArrayList<>();
    }
    
    public String getUsername() {
        return username;
    }

    public String getCredentials() {
        return credentials;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

}
