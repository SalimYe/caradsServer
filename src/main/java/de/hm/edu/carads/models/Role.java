package de.hm.edu.carads.models;

/**
 *
 * @author florian
 */
public class Role extends Model {
    private String name;

    public Role(String name) {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
