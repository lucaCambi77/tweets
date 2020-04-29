package org.cambi.test.tweets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.cambi.application.Application;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.model.UserTweet;
import org.cambi.service.ITwitterService;
import org.cambi.test.config.ApplicationConfigurationTest;
import org.cambi.test.config.dbunit.JsonDataSetLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class, ApplicationConfigurationTest.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/test.properties")
@ActiveProfiles("test")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = {"dataSource"}, dataSetLoader = JsonDataSetLoader.class)
// TODO Dbunit schema handling, @DatabaseTearDown not working
public class TwitterIntegrationTest extends Constant {

    private static final Logger log = LoggerFactory.getLogger(TwitterIntegrationTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ITwitterService twitterService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DatabaseSetups({
            @DatabaseSetup(type = DatabaseOperation.DELETE_ALL),
            @DatabaseSetup(value = "classpath:sample.json", connection = "dataSource", type = DatabaseOperation.INSERT)

    })
    @Transactional(readOnly = true)
    public void should_match_database_status() throws Exception {
        List<Run> aRun = twitterService.findAllRun();

        assertEquals(1, aRun.size());
        assertEquals(1, aRun.get(0).getRunId());
        assertEquals("?track=trump", aRun.get(0).getApiQuery());
        assertEquals(1, aRun.get(0).getUserTweets().size());

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        log.info(objectMapper.writeValueAsString(aRun.get(0)));
    }

    @Test
    public void should_get_greeting_from_api_call() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + this.port + "/", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    @DatabaseSetups({
            @DatabaseSetup(type = DatabaseOperation.DELETE_ALL)

    })
    public void should_create_run_from_api_call() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + this.port + "/run",
                String.class);
        log.info(entity.getBody());

        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    @DatabaseSetups({
            @DatabaseSetup(type = DatabaseOperation.DELETE_ALL)

    })
    public void should_find_runs_from_api_call() throws JsonProcessingException {
        ResponseEntity<String> run = restTemplate.getForEntity("http://localhost:" + this.port + "/run",
                String.class);
        assertEquals(HttpStatus.OK, run.getStatusCode());

        LinkedHashMap<BigInteger, List<UserTweet>> list = objectMapper.readValue(run.getBody(), new TypeReference<LinkedHashMap<BigInteger, List<UserTweet>>>() {
        });

        assertEquals(3, list.size());

        ResponseEntity<String> findAll = restTemplate.getForEntity("http://localhost:" + this.port + "/run/list",
                String.class);

        assertEquals(HttpStatus.OK, findAll.getStatusCode());

        log.info(findAll.getBody());

        List<Run> findAllRun = objectMapper.readValue(findAll.getBody(), new TypeReference<List<Run>>() {
        });

        assertEquals(1, findAllRun.size());

        assertEquals(3, findAllRun.get(0).getUserTweets().size());

    }
}
