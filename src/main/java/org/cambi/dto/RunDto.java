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
public class RunDto implements Cloneable {

    private Long runId;
    private Set<TweetDto> tweets;
    private Date ins;
    private long runTime;
    private int numTweet;
    private String api;
    private String apiQuery;
    private String exception;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
