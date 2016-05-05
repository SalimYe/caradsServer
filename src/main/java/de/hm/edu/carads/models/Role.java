package de.hm.edu.carads.models;

import de.hm.edu.carads.models.util.Model;

/**
 *
 * @author florian
 */
public class Role extends Model {
    private String name;

    public Role(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
