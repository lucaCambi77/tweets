/**
 *
 */
package org.cambi.dao;

import org.cambi.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author luca
 *
 */
@Component
public interface RunDao extends JpaRepository<Run, Long> {


}
