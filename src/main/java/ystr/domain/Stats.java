package ystr.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class Stats {

    @GraphId
    private Long id;

    private int singletons;

    public Stats() {}

    public Long getId() { return id; }

    public int getSingletons() { return singletons; }
}
