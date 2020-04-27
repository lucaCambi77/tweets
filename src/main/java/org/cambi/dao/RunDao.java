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

    default Run saveRun(Run newRun, Long elapse, String endPoint, String query, int tweetSize) {

        newRun.setApi(endPoint);
        newRun.setApiQuery(query);
        newRun.setIns(new Date());
        newRun.setNumTweet(tweetSize);
        newRun.setRunTime(elapse);

        return save(newRun);
    }

}
