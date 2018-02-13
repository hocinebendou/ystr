package ystr.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class DYS710 {

	@GraphId
	private Long id;
	
	private String val;

	private float leftLimit;

	public DYS710() {}

	public DYS710(String val, float leftLimit) {
		this.val = val;
		this.leftLimit = leftLimit;
	}

	@Relationship(type = "HAS_LOCUS_DYS710", direction = Relationship.INCOMING)
	private List<Person> persons = new ArrayList<>();

	public Long getId() { return id; }

	public String getVal() { return val; }

	public float getLeftLimit() { return leftLimit; }

	public void setVal( String val ) { this.val = val; }

	public void setLeftLimit( float leftLimit ) { this.leftLimit = leftLimit; }
}
