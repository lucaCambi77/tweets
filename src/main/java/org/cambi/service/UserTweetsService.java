package org.cambi.service;

import org.cambi.dao.UserTweetDao;
import org.cambi.model.UserTweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTweetsService {

    @Autowired
    private UserTweetDao userDao;

    @Transactional(readOnly = true)
    public List<UserTweet> findUserTweetsByRun(Long runId) {
        return userDao.findByRun(runId, Sort.by(Sort.Order.asc("creationDate")
                , Sort.Order.asc("id.messageId")));
    }

}
