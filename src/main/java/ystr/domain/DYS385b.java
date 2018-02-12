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
public class DYS385b {
    @GraphId
    private Long id;

    private String val;

    public DYS385b() {}

    public DYS385b(String val) { this.val = val; }

    @Relationship(type = "HAS_LOCUS_DYS385b", direction = Relationship.INCOMING)
    private List<Person> persons = new ArrayList<>();

    public Long getId() { return id; }

    public String getVal() { return val; }

    public void setVal( String val ) { this.val = val; }
}
