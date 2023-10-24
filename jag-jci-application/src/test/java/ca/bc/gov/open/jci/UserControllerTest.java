package ca.bc.gov.open.jci;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.jci.common.user.login.*;
import ca.bc.gov.open.jci.common.user.mapping.*;
import ca.bc.gov.open.jci.controllers.UserController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Collections;
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
public class UserControllerTest {

    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getParticipantInfoTest() throws JsonProcessingException {
        var req = new GetParticipantInfo();
        req.setGuid("A");

        var out = new GetParticipantInfoResponse();
        out.setPartId("A");
        out.setPartyName("A");

        ResponseEntity<GetParticipantInfoResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetParticipantInfoResponse>>any()))
                .thenReturn(responseEntity);

        UserController userController = new UserController(restTemplate, objectMapper);
        var resp = userController.getParticipantInfo(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void getNewParticipantInfoTest() throws JsonProcessingException {
        var req = new ca.bc.gov.open.jci.common.participant.info.GetParticipantInfo();
        req.setGUID("A");

        var out = new ca.bc.gov.open.jci.common.participant.info.GetParticipantInfoResponse();
        var outer = new ca.bc.gov.open.jci.common.participant.info.GetParticipantInfoResponseEx();
        out.setGetParticipantInfoResponse(outer);
        outer.setAgenID("A");
        outer.setPartID("A");
        outer.setRoleCd("A");
        outer.setSubRoleCd("A");
        outer.setSubRoleCd("A");

        ResponseEntity<ca.bc.gov.open.jci.common.participant.info.GetParticipantInfoResponseEx>
                responseEntity = new ResponseEntity<>(outer, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito
                                .<Class<
                                                ca.bc.gov.open.jci.common.participant.info
                                                        .GetParticipantInfoResponseEx>>
                                        any()))
                .thenReturn(responseEntity);

        UserController userController = new UserController(restTemplate, objectMapper);
        var resp = userController.getNewParticipantInfo(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void mapGuidToParticipantTest() throws JsonProcessingException {
        var req = new MapGuidToParticipant();
        req.setGuid("A");
        req.setIdirId("A");
        req.setPartId("A");

        var out = new MapGuidToParticipantResponse();
        out.setSuccess("A");

        ResponseEntity<MapGuidToParticipantResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<MapGuidToParticipantResponse>>any()))
                .thenReturn(responseEntity);

        UserController userController = new UserController(restTemplate, objectMapper);
        var resp = userController.mapGuidToParticipant(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void getUserLoginTest() throws JsonProcessingException {
        var req = new GetUserLogin();
        var one = new GetUserLoginRequestType();
        one.setDomainNm("A");
        one.setDomainUserGuid("A");
        one.setDomainUserId("A");
        one.setLoginDtm(Instant.now());
        one.setDeviceNm("A");
        one.setIpAddressTxt("A");
        one.setTemporaryAccessGuid("A");

        req.setRequest(one);

        var out = new GetUserLoginResponseType();
        out.setResponseCd("A");
        out.setResponseMessageTxt("A");
        out.setUserPartId("A");
        out.setUserDefaultAgencyCd("A");
        out.setUserNm("A");
        LoginHistoryType ht = new LoginHistoryType();
        ht.setLoginDtm(Instant.now());
        ht.setLoginDayOfWeek("A");
        ht.setDeviceNm("A");
        ht.setIpAddressTxt("A");

        out.getLoginHistory().add(ht);

        ResponseEntity<GetUserLoginResponseType> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetUserLoginResponseType>>any()))
                .thenReturn(responseEntity);

        UserController userController = new UserController(restTemplate, objectMapper);
        var resp = userController.getUserLogin(req);

        Assertions.assertNotNull(resp);
    }
}
