INSERT INTO TWEET.RUN 
            (runId, 
             ins, 
             runTime, 
             numTweet,
             api,
             apiQuery,
             tweetRuns,
             exception
             ) 
VALUES      (1, 
             '2004-12-31', 
             '10',
             5,
             'http://localhost:8080',
             '?track=bieber',
             null,
             null); 