package de.hm.edu.carads.models;

import de.hm.edu.carads.models.util.Helper;

/**
 *
 * @author florian
 */
public class AdvertiserRegistration extends Advertiser implements Registration {

    private String password;

    public AdvertiserRegistration(String password, String email, String firstName, String lastName) throws Exception {
        super(email, firstName, lastName);
        this.password = Helper.getShaHash(password);
    }
    
    @Override
    public String getPasswordHash() {
        return password;
    }

    @Override
    public void setPassword(String password) throws Exception {
        this.password = Helper.getShaHash(password);
    }

    @Override
    public String getUsername() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
