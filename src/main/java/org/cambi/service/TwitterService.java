package org.cambi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cambi.constant.Constant;
import org.cambi.dao.RunDao;
import org.cambi.dao.TweetDao;
import org.cambi.dao.UserTweetDao;
import org.cambi.dto.RunDto;
import org.cambi.dto.TweetDto;
import org.cambi.dto.UserTweetDto;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.model.UserTweetId;
import org.cambi.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
@Slf4j
public class TwitterService extends Constant {

    private TwitterParserService twitterParserService;
    private TweetDao tweetDao;
    private RunDao runDao;
    private UserTweetDao userDao;

    @Transactional(readOnly = true)
    public List<Run> findAllRun() {
        return runDao.findAll(Sort.by(Sort.Direction.DESC, "runTime"));
    }

    @Transactional
    public Run createRun(String api, String query) throws ExecutionException, InterruptedException {
        Date start = new Date();

        RunDto response = twitterParserService.parseTweetsFrom(api.concat(query));

        return createRun(response.getTweets(), new Date().getTime() - start.getTime(), api, query);
    }

    @Transactional
    private Run createRun(Set<TweetDto> tweetDto, Long elapse, String endPoint, String query) {

        Map<UserTweetDto, List<TweetDto>> tweets = Utils.tweetsToUserTweet(tweetDto);

        Run savedRun = runDao.save(
                Run.builder()
                        .api(endPoint).
                        apiQuery(query).
                        ins(new Date()).
                        numTweet(tweetDto.size())
                        .runTime(elapse)
                        .build());

        createUserTweets(tweets, savedRun);

        return savedRun;
    }

    private void createUserTweets(Map<UserTweetDto, List<TweetDto>> tweets, Run savedRun) {
        for (Map.Entry<UserTweetDto, List<TweetDto>> listByUser : tweets.entrySet()) {

            UserTweet user = UserTweet.builder()
                    .userName(listByUser.getKey().getUserName())
                    .userScreenName(listByUser.getKey().getUserScreenName())
                    .creationDate(listByUser.getKey().getCreationDate())
                    .run(savedRun)
                    .build();

            for (TweetDto tweet : listByUser.getValue()) {

                TweetRun tweetPost = TweetRun.builder()
                        .creationDate(tweet.getCreationDate())
                        .messageText(tweet.getMessageText())
                        .messageId(tweet.getId())
                        .build();

                tweetDao.save(tweetPost);

                userDao.save(
                        user.toBuilder()
                                .id(new UserTweetId(listByUser.getKey().getId(), tweetPost)).build());

            }
        }
    }

}
