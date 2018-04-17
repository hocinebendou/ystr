package ystr.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ystr.domain.Person;

import java.util.Collection;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long>{

	Collection<Person> findAll();

	@Query("OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n1:DYS710) " +
			"WITH collect(distinct n1) as c1 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n2:DYS518) " +
			"WITH collect(distinct n2) + c1 as c2 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n3:DYS385a) " +
			"WITH collect(distinct n3) + c2 as c3 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n4:DYS385b) " +
			"WITH collect(distinct n4) + c3 as c4 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n5:DYS644) " +
			"WITH collect(distinct n5) + c4 as c5 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n6:DYS612) " +
			"WITH collect(distinct n6) + c5 as c6 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n7:DYS626) " +
			"WITH collect(distinct n7) + c6 as c7 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n8:DYS504) " +
			"WITH collect(distinct n8) + c7 as c8 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n9:DYS481) " +
			"WITH collect(distinct n9) + c8 as c9 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n10:DYS447) " +
			"WITH collect(distinct n10) + c9 as c10 " +
			"OPTIONAL MATCH (p:Person{name: {personName}})-[]->(n11:DYS449) " +
			"WITH collect(distinct n11) + c10 as c11 " +
			"UNWIND c11 as nodes " +
			"RETURN nodes.val")
	Collection<String> haplotypeValues(@Param("personName") String personName);


	@Query("MATCH (n:Person) RETURN count(n)")
	int countPersons();
	
	@Query("MATCH (p:Person) " +
		   "MATCH (y1:DYS710 {val: {dys710Val}}) " +
		   "MATCH (y2:DYS518 {val: {dys518Val}}) " +
		   "MATCH (y3:DYS385a {val: {dys385aVal}}) " +
		   "MATCH (y4:DYS385b {val: {dys385bVal}}) " +
		   "MATCH (y5:DYS644 {val: {dys644Val}}) " +
		   "MATCH (y6:DYS612 {val: {dys612Val}}) " +
		   "MATCH (y7:DYS626 {val: {dys626Val}}) " +
		   "MATCH (y8:DYS504 {val: {dys504Val}}) " +
		   "MATCH (y9:DYS481 {val: {dys481Val}}) " +
		   "MATCH (y10:DYS447 {val: {dys447Val}}) " +
		   "MATCH (y11:DYS449 {val: {dys449Val}}) " +
		   "WHERE (p)-[:HAS_LOCUS_DYS710]->(y1) " +
		   "AND (p)-[:HAS_LOCUS_DYS518]->(y2) " +
		   "AND (p)-[:HAS_LOCUS_DYS385a]->(y3) " +
		   "AND (p)-[:HAS_LOCUS_DYS385b]->(y4) " +
		   "AND (p)-[:HAS_LOCUS_DYS644]->(y5) " +
		   "AND (p)-[:HAS_LOCUS_DYS612]->(y6) " +
		   "AND (p)-[:HAS_LOCUS_DYS626]->(y7) " +
		   "AND (p)-[:HAS_LOCUS_DYS504]->(y8) " +
		   "AND (p)-[:HAS_LOCUS_DYS481]->(y9) " +
		   "AND (p)-[:HAS_LOCUS_DYS447]->(y10) " +
		   "AND (p)-[:HAS_LOCUS_DYS449]->(y11) " +
		   "RETURN count(p)")
	int countYPerson(@Param("dys710Val") String dys710Val, 
					 @Param("dys518Val") String dys518Val,
					 @Param("dys385aVal") String dys385aVal,
					 @Param("dys385bVal") String dys385bVal,
					 @Param("dys644Val") String dys644Val,
					 @Param("dys612Val") String dys612Val,
					 @Param("dys626Val") String dys626Val,
					 @Param("dys504Val") String dys504Val,
					 @Param("dys481Val") String dys481Val,
					 @Param("dys447Val") String dys447Val,
					 @Param("dys449Val") String dys449Val);
}
