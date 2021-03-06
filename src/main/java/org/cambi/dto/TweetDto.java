package org.cambi.dto;

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
public class TweetDto extends Constant {

    @JsonProperty(MESSAGE_ID)
    private BigInteger id;
    @JsonProperty(CREATED_AT)
    private Date creationDate;
    @JsonProperty(TEXT)
    private String messageText;
    @JsonProperty(USER)
    private UserTweetDto userTweet;

    public UserTweetDto getUserTweet() {
        return userTweet == null ? UserTweetDto.builder()
                .id(new BigInteger("0"))
                .userName("N.A")
                .userScreenName("N.A")
                .creationDate(new Date(0))
                .build() : userTweet;
    }
}
