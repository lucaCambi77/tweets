
## Task ##

+ Connect to the [Twitter Streaming API](https://dev.twitter.com/streaming/overview)
    * Use the following values:
        + Consumer Key: `RLSrphihyR4G2UxvA0XBkLAdl`
        + Consumer Secret: `FTz2KcP1y3pcLw0XXMX5Jy3GTobqUweITIFy4QefullmpPnKm4`
    * You will need to login with Twitter
+ Filter messages that track on "bieber"
+ Retrieve the incoming messages for 30 seconds or up to 100 messages, whichever comes first
+ Your application should return the messages grouped by user (users sorted chronologically, ascending)
+ The messages per user should also be sorted chronologically, ascending
+ For each message, we will need the following:
    * The message ID
    * The creation date of the message as epoch value
    * The text of the message
    * The author of the message
+ For each author, we will need the following:
    * The user ID
    * The creation date of the user as epoch value
    * The name of the user
    * The screen name of the user

## Install and run the application
To use the application you need java 8 and docker installed in your machine.

There are two profiles with which you can run the application

## no-docker (running as standalone application)

To install 

	- mvn clean install

To run  

	- java -jar target/*.jar
		
## docker (running inside docker container)

To install

	- mvn clean install -Pdocker

To run

	- docker run -p 8080:8080 -v /tmp:/tmp -ti --name bieber-tweets org.cambi/bieber-tweets

There are 2 api that are exposed :

## http://localhost:8080/run
To launch a run in order to retrieve tweets
## http://localhost:8080/run/list
To see information about multiple run of the applications






