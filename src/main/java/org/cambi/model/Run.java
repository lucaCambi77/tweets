package org.cambi.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private Set<UserTweet> userTweets = new HashSet<UserTweet>(0);
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "run", targetEntity = UserTweet.class)
	@JsonIgnore
	public Set<UserTweet> getUserTweets() {
		return userTweets;
	}

	public void setUserTweets(Set<UserTweet> userTweets) {
		this.userTweets = userTweets;
	}

	@Column
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}