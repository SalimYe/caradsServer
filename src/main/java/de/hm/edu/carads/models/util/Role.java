package de.hm.edu.carads.models.util;

/**
 * Eine Rolle beschreibt den Status eines Benutzers und seine Berechtigung.
 * @author FS
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
