package de.hm.edu.carads.models;

import de.hm.edu.carads.models.util.Helper;

/**
 *
 * @author florian
 */
public class DriverRegistration extends Driver implements Registration {

    private String password;

    public DriverRegistration(String password, String email, String firstName, String lastName) {
        super(email, firstName, lastName);
        this.password = password;
    }

    @Override
    public String getPasswordHash() throws Exception {
        return Helper.getShaHash(this.password);
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
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
