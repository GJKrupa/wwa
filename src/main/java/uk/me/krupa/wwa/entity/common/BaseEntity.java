package uk.me.krupa.wwa.entity.common;

import org.springframework.data.neo4j.annotation.GraphId;

/**
 * Created by krupagj on 27/03/2014.
 */
public abstract class BaseEntity {

    @GraphId
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
