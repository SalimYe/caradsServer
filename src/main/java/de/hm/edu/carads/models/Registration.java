package de.hm.edu.carads.models;

import java.util.Collection;

/**
 *
 * @author florian
 */
public interface Registration {
    
    public String getUsername();
    public String getPasswordHash();
    public void setUsername(String username);
    public void setPassword(String password) throws Exception;
    
}
