package org.cambi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.cambi.constant.Constant;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTweetDto extends Constant {

    @JsonProperty(USER_ID)
    private BigInteger id;
    @JsonProperty(CREATED_AT)
    private Date creationDate;
    @JsonProperty(USER_NAME)
    private String userName;
    @JsonProperty(SCREEN_NAME)
    private String userScreenName;
    @JsonIgnore
    private TweetDto tweetRuns;

}
