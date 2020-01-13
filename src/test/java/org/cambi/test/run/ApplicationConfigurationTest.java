/**
 * 
 */
package org.cambi.test.run;

import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.repository.UserRepository;
import org.cambi.service.TwitterService;
import org.cambi.test.config.StatusRun;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;

/**
 * @author luca
 *
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableTransactionManagement
@Profile("test")
@ComponentScan(basePackageClasses = TwitterService.class)
public class ApplicationConfigurationTest
{
    @Mock
    private TwitterAuthenticator authenticator;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public TwitterAuthenticator getTwitterAuthenticator() throws TwitterAuthenticationException, IOException
    {
        MockitoAnnotations.initMocks(this);

        /**
         * We read tweets from a file to create a fake request
         * 
         * Mocking google Transport
         */
        HttpTransport transport = new MockHttpTransport()
        {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException
            {
                return new MockLowLevelHttpRequest()
                {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException
                    {

                        InputStream is = new FileInputStream("src/test/resources/tweets.json");
                        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

                        String line = buf.readLine();
                        StringBuilder sb = new StringBuilder();

                        while (line != null)
                        {
                            sb.append(line).append("\n");
                            line = buf.readLine();
                        }
                        buf.close();

                        StatusRun status = objectMapper.readValue(sb.toString(), StatusRun.class);
                        sb = new StringBuilder();

                        for (TweetRun tweet : status.getTweets())
                        {
                            sb.append(objectMapper.writeValueAsString(tweet)).append("\n");
                        }

                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.setStatusCode(200);
                        response.setContentType(Json.MEDIA_TYPE);
                        response.setContent(sb.toString());
                        return response;
                    }
                };
            }
        };

        when(authenticator.getAuthorizedHttpRequestFactory()).thenReturn(transport.createRequestFactory());
        return authenticator;
    }

    @Autowired
    private Environment env;

    @Bean
    @Profile("test")
    public DataSource dataSource()
    {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;INIT=CREATE SCHEMA IF NOT EXISTS TWEET;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { Run.class.getPackage().getName() });
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory)
    {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    final Properties additionalProperties()
    {
        final Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.ddl-auto"));
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));

        return hibernateProperties;
    }
}
