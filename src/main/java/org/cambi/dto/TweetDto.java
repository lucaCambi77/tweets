package org.cambi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.cambi.constant.Constant;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
public class TweetDto extends Constant {

    @JsonProperty(MESSAGE_ID)
    private BigInteger id;
    @JsonProperty(CREATED_AT)
    private Date creationDate;
    @JsonProperty(TEXT)
    private String messageText;
    @JsonProperty(USER)
    private UserTweetDto userTweet;

}
