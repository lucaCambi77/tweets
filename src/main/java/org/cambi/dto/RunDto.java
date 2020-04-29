/**
 *
 */
package org.cambi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

/**
 * @author luca
 *
 */
@Getter
@Setter
public class RunDto {

    private Long runId;
    private Set<TweetDto> tweets;
    private Date ins;
    private long runTime;
    private int numTweet;
    private String api;
    private String apiQuery;
    private String exception;
}
