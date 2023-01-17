package ca.bc.gov.open.ccd;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.ccd.common.dev.utils.ClearAppearanceResults;
import ca.bc.gov.open.ccd.common.dev.utils.ClearAppearanceResultsResponse;
import ca.bc.gov.open.ccd.common.dev.utils.RecreateCourtList;
import ca.bc.gov.open.ccd.common.dev.utils.RecreateCourtListResponse;
import ca.bc.gov.open.ccd.controllers.DevUtilsController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DevUtilsControlTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void clearAppearanceResultsTest() throws JsonProcessingException {

        var req = new ClearAppearanceResults();

        var out = new ClearAppearanceResultsResponse();
        out.setStatus("A");

        ResponseEntity<ClearAppearanceResultsResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<ClearAppearanceResultsResponse>>any()))
                .thenReturn(responseEntity);

        DevUtilsController devUtilsController = new DevUtilsController(restTemplate, objectMapper);
        var resp = devUtilsController.clearAppearanceResults(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void recreateCourtListTest() throws JsonProcessingException {

        var req = new RecreateCourtList();

        var out = new RecreateCourtListResponse();
        out.setStatus("A");

        ResponseEntity<RecreateCourtListResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<RecreateCourtListResponse>>any()))
                .thenReturn(responseEntity);

        DevUtilsController devUtilsController = new DevUtilsController(restTemplate, objectMapper);
        var resp = devUtilsController.reCreateCourtList(req);

        Assertions.assertNotNull(resp);
    }
}
