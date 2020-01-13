/**
 * 
 */
package org.cambi.repository;

import org.cambi.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author luca
 *
 */
@Component	
public interface RunRepository extends JpaRepository<Run, Long> {
	
	
}
