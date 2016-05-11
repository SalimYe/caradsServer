package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;
import de.hm.edu.carads.models.util.Model;

/**
 *
 * @author florian
 */
public class User extends Model{

    private String username;
    private String credentials;
    private Collection <Role> roles;

    public User(String username, String credentials, String role, String roleId) {
        this.username = username;
        this.credentials = credentials;
        this.roles = new ArrayList<>();
        this.roles.add(new Role(role, roleId));
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
    
    public void addRole(String role, String roleId) {
        this.roles.add(new Role(role, roleId));
    }
}
