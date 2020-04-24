
package org.cambi.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.model.UserTweetId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 *
 */
public class ObjectMapperFactory extends Constant {

	@JsonFilter("Filter")
	public class PropertyFilterMixIn {
	}

	private ObjectMapper objectMapper;

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public ObjectMapperFactory() {
		this.objectMapper = new ObjectMapper();

		/**
		 * Object mapper serialization properties
		 */
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		// objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		objectMapper.addMixIn(TweetRun.class, TweetRunMixIn.class);
		objectMapper.addMixIn(UserTweet.class, UserTweetMixIn.class);
		objectMapper.addMixIn(Run.class, RunMixIn.class);

		DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		objectMapper.setDateFormat(dateFormat);
	}

	/**
	 * @author luca
	 *
	 */
	@JsonSerialize(using = UserSerializer.class)
	@JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = UserTweet.class)
	public interface UserTweetMixIn {

		@JsonDeserialize(using = UserIdDeserializer.class)
		@JsonProperty(USER_ID)
		public UserTweetId getId();

		@JsonProperty(USER_NAME)
		public String getUserName();

		@JsonProperty(SCREEN_NAME)
		public String getUserSreenName();

		@JsonProperty(CREATED_AT)
		public Date getCreationDate();
	}

	/**
	 * @author luca
	 *
	 */
	@JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id", scope = TweetRun.class)
	public interface TweetRunMixIn {

		@JsonProperty(MESSAGE_ID)
		public Long getId();

		@JsonProperty(USER)
		public UserTweet getUserTweets();

		@JsonProperty(TEXT)
		public String getMessageText();

		@JsonProperty(CREATED_AT)
		public Date getCreationDate();

		@JsonIgnore
		public Run getRun();
	}

	/**
	 * @author luca
	 *
	 */
	@JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "runId", scope = Run.class)
	public interface RunMixIn {

		@JsonProperty(RUN_ID)
		public Long getRunId();

		@JsonProperty(CREATED_AT)
		public Date getIns();

		@JsonProperty(RUN_TIME)
		public long getRunTime();

		@JsonProperty(NUM_TWEET)
		public int getNumTweet();

		public Set<TweetRun> getTweetRuns();

		@JsonProperty(EXCEPTION)
		public String getException();
	}
}
