package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;
import de.hm.edu.carads.models.util.Model;
import de.hm.edu.carads.models.util.Role;

/**
 *
 * @author florian
 */
public class User extends Model{

    private String username;
    private String credentials;
    private Collection <String> roles;

    public User(String username, String credentials, String role, String roleId) {
    	this.id = roleId;
        this.username = username;
        this.credentials = credentials;
        this.roles = new ArrayList<>();
        this.roles.add(role);
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return credentials;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String credentials) {
        this.credentials = credentials;
    }
}
