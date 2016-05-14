package de.hm.edu.carads.models.util;

/**
 *
 * @author florian
 */
public class Role {
    private String name;
    private String roleId;

    public Role(String name, String roleId) {
        super();
        this.name = name;
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
