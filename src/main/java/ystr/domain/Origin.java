package ystr.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class Origin {
    @GraphId
    private Long id;

    private String group;
    private String region;

    public Origin(){}

    public Long getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getRegion() {
        return region;
    }
}
