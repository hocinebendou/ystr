package ystr.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class DYS644 {
    @GraphId
    private Long id;

    private String val;

    private float leftLimit;

    public DYS644() {}

    public DYS644(String val, float leftLimit) {
        this.val = val;
        this.leftLimit = leftLimit;
    }

    @Relationship(type = "HAS_LOCUS_DYS644", direction = Relationship.INCOMING)
    private List<Person> persons = new ArrayList<>();

    public Long getId() { return id; }

    public String getVal() { return val; }

    public float getLeftLimit() { return leftLimit; }

    public void setVal( String val ) { this.val = val; }

    public void setLeftLimit( float leftLimit ) { this.leftLimit = leftLimit; }
}
