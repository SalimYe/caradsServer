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
    private String password_hased;
    private Collection <Role> roles;

    public User(String username, String credentials, String role, String roleId) {
        this.username = username;
        this.password_hased = credentials;
        this.roles = new ArrayList<>();
        this.roles.add(new Role(role, roleId));
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password_hased;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String credentials) {
        this.password_hased = credentials;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
    
    public void addRole(String role, String roleId) {
        this.roles.add(new Role(role, roleId));
    }
}
