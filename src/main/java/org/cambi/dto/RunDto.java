/**
 *
 */
package org.cambi.dto;

import lombok.*;

import java.util.Date;
import java.util.Set;

/**
 * @author luca
 *
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
