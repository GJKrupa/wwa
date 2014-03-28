package uk.me.krupa.wwa.entity.user;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.security.core.GrantedAuthority;
import uk.me.krupa.wwa.entity.common.BaseEntity;

/**
 * Created by krupagj on 25/03/2014.
 */
@NodeEntity
public class UserAuthority extends BaseEntity implements GrantedAuthority {

    public static final String ROLE_USER = "ROLE_USER";

    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return getPermission();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GrantedAuthority) {
            return getAuthority().equals(((GrantedAuthority) o).getAuthority());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return permission.hashCode();
    }
}
