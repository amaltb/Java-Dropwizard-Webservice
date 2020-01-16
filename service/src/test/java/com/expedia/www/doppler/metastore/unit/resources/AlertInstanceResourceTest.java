package com.expedia.www.doppler.metastore.unit.resources;

import com.expedia.www.doppler.metastore.commons.entities.AlertInstance;
import com.ab.example.metastore.service.dao.AlertInstanceDao;
import com.ab.example.metastore.service.resources.AlertInstanceResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static util.TestUtil.readJsonFileToString;

/**
 * @author _amal
 * <p>
 * Unit test suite for AlertInstanceResource.
 */
public class AlertInstanceResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertInstanceResourceTest.class);
    private static final AlertInstanceDao MOCK_DAO = mock(AlertInstanceDao.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new AlertInstanceResource(MOCK_DAO))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();
    private final AlertInstance alertInstance = getAlertInstance(readJsonFileToString("data/alert_instance.json"));

    @Before
    public void setup() {
        when(MOCK_DAO.find(0)).thenReturn(alertInstance);
    }

    @After
    public void tearDown() {
        // we have to reset the mock after each test because of the
        // @ClassRule, or use a @Rule as mentioned below.
        reset(MOCK_DAO);
    }

    @Test
    public void testAlertInstance() {
        final AlertInstance instance = RESOURCES.target("/api/v1/alert/0").request().get(AlertInstance.class);
        assertThat(instance).isEqualTo(alertInstance);
        verify(MOCK_DAO).find(0);
    }

    private AlertInstance getAlertInstance(String s) {
        try {
            return mapper.readValue(s, AlertInstance.class);
        } catch (IOException e) {
            LOGGER.error("Could not parse alert_instance from json due to exception.", e);
        }
        return null;
    }

}
