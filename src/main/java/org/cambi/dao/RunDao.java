package org.cambi.dao;

import org.cambi.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author luca
 */
@Component
public interface RunDao extends JpaRepository<Run, Long> {

    default Run saveRun(Long elapse, String endPoint, String query, int tweetsNum) {

        return save(
                Run.builder()
                        .api(endPoint).
                        apiQuery(query).
                        ins(new Date()).
                        numTweet(tweetsNum).
                        runTime(elapse)
                        .build());
    }

}
