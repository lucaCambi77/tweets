package org.cambi.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Serialize class for run
 *
 * @author luca
 */
@Entity
@Table(name = "RUN")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "runId")
public class Run implements java.io.Serializable {

	private Long runId;
	private Date ins;
	private long runTime;
	private int numTweet;
	private String api;
	private String apiQuery;
	private Set<TweetRun> tweetRuns = new HashSet<TweetRun>(0);
	private String exception;

	@Id
	@SequenceGenerator(name = "rungenerator", sequenceName = "RUN_SEQUENCE", allocationSize = 1)
	@GeneratedValue(strategy = SEQUENCE, generator = "rungenerator")
	@Column(nullable = true, precision = 10, scale = 0)
	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	@Column(nullable = true, length = 7)
	public Date getIns() {
		return ins;
	}

	public void setIns(Date ins) {
		this.ins = ins;
	}

	@Column(nullable = false)
	public long getRunTime() {
		return runTime;
	}

	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}

	@Column(nullable = false)
	public int getNumTweet() {
		return numTweet;
	}

	public void setNumTweet(int numTweet) {
		this.numTweet = numTweet;
	}

	@Column(nullable = false)
	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	@Column(nullable = false)
	public String getApiQuery() {
		return apiQuery;
	}

	public void setApiQuery(String apiQuery) {
		this.apiQuery = apiQuery;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "run", targetEntity = TweetRun.class)
	public Set<TweetRun> getTweetRuns() {
		return tweetRuns;
	}

	public void setTweetRuns(Set<TweetRun> tweetRuns) {
		this.tweetRuns = tweetRuns;
	}

	@Column
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Run))
			return false;
		Run castOther = (Run) other;

		return ((this.getRunId() == castOther.getRunId()) || (this.getRunId() != null && castOther.getRunId() != null
				&& this.getRunId().equals(castOther.getRunId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getRunId() == null ? 0 : this.getRunId().hashCode());
		return result;
	}

}
