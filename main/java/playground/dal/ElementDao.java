package playground.dal;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import playground.logic.ElementEntity;

@RepositoryRestResource
public interface ElementDao extends PagingAndSortingRepository<ElementEntity,String>{

	public ArrayList<ElementEntity> findAllByCreatorPlaygroundAndCreatorEmail( 
			@Param("creatorPlayground") String creatorPlayground, 
			@Param("creatorEmail") String creatorEmail, 
			Pageable pageable);
	
	public ArrayList<ElementEntity> findAllByXBetweenAndYBetween(
    		@Param("x1") double d,
    		@Param("x2") double e,
    		@Param("y1") double f,
    		@Param("y2") double g,
    		Pageable pageable);


//	@Query(value = "from ELEMENT s join s.attributes a where a.name = ?1 AND a.value= ?1")
//	public ArrayList<ElementEntity> findAllByAttributNameAndAttributeValue(
//			String attributeName,
//			Object attributeValue,
//			Pageable pageable);
}
	
