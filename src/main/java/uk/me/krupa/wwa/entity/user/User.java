package uk.me.krupa.wwa.entity.user;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;
import uk.me.krupa.wwa.entity.common.BaseEntity;

import java.util.*;

/**
 * Created by krupagj on 24/03/2014.
 */
@NodeEntity
public class User extends BaseEntity implements UserDetails, SocialUserDetails {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String fullName;
    private String imageUrl;

    @RelatedTo(type = "IS_TYPE")
    @Fetch
    private Set<UserAuthority> grantedAuthorities;

    private List<SimpleGrantedAuthority> simpleGrantedAuthorities;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<UserAuthority> getGrantedAuthorities() {
        if (grantedAuthorities == null) {
            grantedAuthorities = new HashSet<>();
        }
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(Set<UserAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (simpleGrantedAuthorities == null) {
            simpleGrantedAuthorities = new LinkedList<>();
            for (GrantedAuthority auth: getGrantedAuthorities()) {
                simpleGrantedAuthorities.add(new SimpleGrantedAuthority(auth.getAuthority()));
            }
        }
        return simpleGrantedAuthorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUserId() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
