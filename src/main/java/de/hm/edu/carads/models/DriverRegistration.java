package de.hm.edu.carads.models;

import de.hm.edu.carads.models.util.Helper;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author florian
 */
public class DriverRegistration extends Driver implements Registration {

    private String password;

    public DriverRegistration(String password, String email, String firstName, String lastName) throws Exception {
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
    

    public Driver getDriver() {
        return new Driver(this.getBirthdate(), this.getOccupation(), this.getLicenseDate(), this.getCars(), this.getProfilePicture(), this.getEmail(), this.firstname, this.getLastName());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public void setUsername(String username) {
        this.email = username;
    }

}
