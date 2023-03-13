package ca.bc.gov.open.jci;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.jci.controllers.CourtController;
import ca.bc.gov.open.jci.court.one.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
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
public class CourtControllerTests {

    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getCrtListTest() throws JsonProcessingException {
        var req = new GetCrtList();
        req.setAgencyIdentifierCd("A");
        req.setRoomCd("A");
        req.setProceedingDate(Instant.now());
        req.setDivisionCd("A");
        req.setFileNumber("A");

        var crl = new CriminalCourtList();
        crl.setCriminalAppearanceID("A");
        crl.setCourtListTypeCd("A");
        crl.setAppearanceSequenceNumber("A");
        crl.setAppearanceTime("A");
        var fi = new FileInformationType();
        fi.setFileLocaAgencyIdentifierCd("A");
        fi.setPhyFileFolderNo("A");
        fi.setPhysTicketSeriesTxt("A");
        fi.setMdocInfoSeqNo("A");
        fi.setPartId("A");
        fi.setProfSeqNo("A");
        fi.setMdocJustinNo("A");
        fi.setCourtLevelCd("A");
        fi.setCourtClassCd("A");
        fi.setMdocTypeCd("A");
        fi.setMdocTypeDsc("A");
        fi.setMdocImageId("A");
        fi.setMdocAmendedYN("A");
        fi.setMdocAmendedText("A");
        var is = new Issue();
        is.setCountPrntSeqNo("A");
        is.setStatuteActCd("A");
        is.setStatuteSectionCd("A");
        is.setStatuteSectionDsc("A");
        fi.setIssue(Collections.singletonList(is));

        crl.setFileInformation(fi);

        crl.setFileNumberText("A");
        crl.setFileHomeLocationName("A");
        crl.setSealTypeCd("A");
        crl.setSealTypeDsc("A");
        crl.setOtherFileInformationText("A");
        crl.setAccusedFullName("A");

        var fn = new AccusedFormattedName();
        fn.setLastName("A");
        fn.setGiven1Name("A");
        fn.setGiven2Name("A");
        fn.setGiven3Name("A");
        fn.setOrgName("A");

        crl.setAccusedFormattedName(fn);

        crl.setAccusedBirthDate("A");
        crl.setAccusedCurrentBailProcessText("A");
        crl.setAccusedInCustodyFlag("A");
        crl.setCounselFullName("A");
        crl.setCounselDesignationYN("A");
        crl.setCounselPartId("A");
        crl.setCaseAgeDaysNumber("A");
        crl.setCrownTypeCd("A");
        crl.setCrownLocationCd("A");
        crl.setParticipantRoleCd("A");
        crl.setParticipantRoleDsc("A");

        var at = new AttendanceMethod();
        at.setApprId("A");
        at.setAssetUsageSeqNo("A");
        at.setRoleType("A");
        at.setAttendanceMethodCd("A");
        at.setPhoneNumber("A");
        at.setInstruction("A");
        at.setOtherRoleName("A");
        crl.setAttendanceMethod(Collections.singletonList(at));

        var ac = new AppearanceCount();
        ac.setAppearanceCountId("A");
        ac.setCountPrintSequenceNumber("A");
        ac.setChargeStatuteCode("A");
        ac.setChargeStatuteDescription("A");
        ac.setLesserIncludedChargeStatuteCode("A");
        ac.setAppearanceCountCancelledYN("A");
        ac.setLesserIncludedChargeStatuteDescription("A");
        ac.setAppearanceReasonCode("A");
        ac.setPleaCode("A");
        ac.setPleaDate(Instant.now());
        ac.setElectionCode("A");
        ac.setElectionDate(Instant.now());
        ac.setMdctSeqNo("A");
        ac.setOffenceAgeDaysNumber("A");
        ac.setIssuingOfficerPoliceForceCode("A");
        ac.setIssuingOfficerPINText("A");
        ac.setIssuingOfficerSurnameName("A");
        ac.setFindingCode("A");
        ac.setFindingDate(Instant.now());

        crl.setAppearanceCount(Collections.singletonList(ac));

        var bn = new Bans();
        bn.setBanTypeCd("A");
        bn.setBanTypeDescription("A");
        bn.setBanTypeAct("A");
        bn.setBanTypeSection("A");
        bn.setBanTypeSubSection("A");
        bn.setBanStatuteId("A");
        bn.setBanCommentText("A");
        bn.setBanAcprId("A");
        crl.setBans(Collections.singletonList(bn));

        var bov = new BailOrderToVary();
        bov.setFormTypeCd("A");
        bov.setDocmTypeDsc("A");
        bov.setDocmId("A");
        bov.setDocmIssueDate(Instant.now());
        bov.setDocmImageId("A");
        bov.setDocmStatus("A");
        crl.setBailOrderToVary(Collections.singletonList(bov));

        var sov = new SentOrderToVary();
        sov.setFormTypeCd("A");
        sov.setDocmTypeDsc("A");
        sov.setDocmId("A");
        sov.setDocmIssueDate(Instant.now());
        sov.setDocmImageId("A");
        sov.setDocmStatus("A");
        crl.setSentOrderToVary(Collections.singletonList(sov));

        var an = new AgeNotice();
        an.setEventDate(Instant.now());
        an.setEventTypeDsc("A");
        an.setDetailText("A");
        an.setDOB(Instant.now());
        an.setRelationship("A");
        an.setProvenBy("A");
        an.setNoticeTo("A");

        crl.setAgeNotice(Collections.singletonList(an));

        var st = new SpeakerType();
        st.setSpeakerId("A");
        st.setSpeakerTypeCd("A");
        st.setSpeakerSeqNo("A");
        st.setVoirDireSeqNo("A");
        st.setSpeakerName("A");
        st.setSpeakerStatusCd("A");
        st.setSpeakerStatusDsc("A");
        SpeakerEvent se = new SpeakerEvent();
        se.setSpeakerEventDate("A");
        se.setSpeakerEventTime("A");
        se.setSpeakerEventText("A");

        st.setSpeakerEvent(Collections.singletonList(se));
        crl.setSpeaker(Collections.singletonList(st));

        ArrestWarrant aw = new ArrestWarrant();
        aw.setFileNumberText("A");
        aw.setWarrantDate(Instant.now());
        crl.setArrestWarrant(Collections.singletonList(aw));

        var crp = new CRProtectionOrderType();
        crp.setPORConditionText("A");
        crp.setOrderTypeDsc("A");
        crp.setPOROrderIssueDate(Instant.now());
        crl.setProtectionOrder(Collections.singletonList(crp));

        var sa = new ScheduledAppearance();
        sa.setAppearanceId("A");
        sa.setCourtAgencyIdentifier("A");
        sa.setCourtRoom("A");
        sa.setAppearanceDate(Instant.now());
        sa.setAppearanceTime(Instant.now());
        sa.setAppearanceReasonCd("A");
        sa.setEstDurationHours("A");
        sa.setEstDurationMins("A");

        var hr = new HearingRestrictionType();
        hr.setHearingRestrictiontype("A");
        hr.setJudgeName("A");
        hr.setHearingRestrictionDate(Instant.now());

        crl.setHearingRestriction(Collections.singletonList(hr));
        crl.setScheduledAppearance(Collections.singletonList(sa));

        var co = new CFCOrderType();
        co.setCFCOrderIssueDate(Instant.now());
        co.setOrderTypeDsc("A");
        co.setCFCConditionText("A");

        crl.setCFCOrder(Collections.singletonList(co));

        var crl2 = new CivilCourtListType();
        crl2.setAppearanceId("A");
        crl2.setAppearanceTime("A");
        crl2.setBinderText("A");
        crl2.setCourtListPrintSortNumber("A");
        crl2.setCivilDocumentsAvailable("A");
        crl2.setAppearanceDate(Instant.now());
        crl2.setExternalFileNumberText("A");
        crl2.setCourtListTypeCd("A");
        crl2.setCourtRoomCd("A");
        crl2.setCourtClassCd("A");
        crl2.setFileAccessLevelCd("A");
        crl2.setSealStartDate(Instant.now());
        crl2.setSealEndDate(Instant.now());
        crl2.setSheriffCommentText("A");
        crl2.setSealFileSOCText("A");
        crl2.setEstimatedAppearanceMinutes("A");

        PhysicalFileType py = new PhysicalFileType();
        py.setPhysicalFileID("A");
        py.setFileAccessLevelCd("A");
        py.setFileNumber("A");
        py.setStyleOfCause("A");
        py.setLeftPartyLastName("A");
        py.setLeftPartyGivenName("A");
        py.setLeftPartyOtherCount("A");
        py.setRightPartyGivenName("A");
        py.setRightPartyLastName("A");
        py.setRightPartyOtherCount("A");
        py.setThirdPartyGivenName("A");
        py.setThirdPartyLastName("A");
        py.setThirdPartyOtherCount("A");
        py.setHomeAgencyCd("A");
        py.setCivilAgencyCd("A");
        crl2.setPhysicalFile(py);

        AssetType as = new AssetType();
        as.setAssetTypeDescription("A");
        crl2.setAsset(Collections.singletonList(as));

        DocumentType dt = new DocumentType();
        dt.setAppearanceID("A");
        dt.setDocumentId("A");
        dt.setFileSeqNumber("A");
        dt.setRoleTypeCode("A");
        dt.setDocumentTypeCd("A");
        dt.setDocumentTypeDescription("A");
        dt.setDocumentAccessLevelCd("A");
        dt.setDocumentSealEndDate(Instant.now());
        dt.setDocumentSealStartDate(Instant.now());
        dt.setDateGranted(Instant.now());
        dt.setEstimatedDocumentMinutes("A");
        dt.setDateVaried(Instant.now());
        dt.setCancelledDate(Instant.now());
        dt.setOrderDocumentYN("A");
        dt.setAdjudicatorPartId("A");
        dt.setAdjudicatorName("A");
        dt.setOrderDocumentYN("A");
        dt.setAppearanceReasonCode("A");

        var is2 = new IssueType();
        is2.setIssueDescription("A");
        is2.setIssueType("A");
        is2.setIssueNumber("A");
        dt.setIssue(Collections.singletonList(is2));

        var ft = new FiledByType();
        ft.setFiledByName("A");
        ft.setRoleTypeCode("A");
        dt.setFiledBy(Collections.singletonList(ft));
        crl2.setDocument(Collections.singletonList(dt));

        var pt = new PartyType();
        pt.setPartyId("A");
        pt.setPartyScheduled("A");
        pt.setPartyRoleType("A");
        pt.setPartyFullName("A");
        pt.setAttendanceMethodCd("A");
        pt.setPartyTypeCd("A");
        pt.setLeftRightParty("A");
        pt.setBirthDate(Instant.now());
        pt.setPhoneNumber("A");
        pt.setInstruction("A");
        pt.setPartyFullAddressText("A");
        pt.setWarrantIssueDate(Instant.now());
        pt.setActiveYN("A");
        CounselType ct = new CounselType();
        ct.setCounselFullName("A");
        ct.setPhoneNumber("A");
        ct.setCounselId("A");

        pt.setCounsel(Collections.singletonList(ct));

        var rt = new RepresentativeType();
        rt.setInstruction("A");
        rt.setAttendanceMethodCd("A");
        rt.setPhoneNumber("A");
        rt.setRepFullName("A");
        pt.setRepresentative(Collections.singletonList(rt));

        var lrt = new LegalRepresentativeType();
        lrt.setLegalRepFullName("A");
        lrt.setLegalRepTypeDsc("A");
        pt.setLegalRepresentative(Collections.singletonList(lrt));

        var pr2 = new PartyRoleType2();
        pr2.setRoleTypeCd("A");
        pr2.setRoleTypeDsc("A");

        pt.setPartyRole(Collections.singletonList(pr2));

        PartyNameType pnt = new PartyNameType();
        pnt.setFirstGivenNm("A");
        pnt.setNameTypeCd("A");
        pnt.setNameTypeDsc("A");
        pnt.setSurnameNm("A");
        pnt.setFirstGivenNm("A");
        pnt.setSecondGivenNm("A");
        pnt.setThirdGivenNm("A");
        pnt.setOrganizationNm("A");
        pt.setPartyName(Collections.singletonList(pnt));

        crl2.setParties(Collections.singletonList(pt));

        SpeakerType st2 = new SpeakerType();
        st2.setSpeakerId("A");
        st2.setSpeakerStatusCd("A");
        st2.setSpeakerSeqNo("A");
        st2.setVoirDireSeqNo("A");
        st2.setSpeakerName("A");
        st2.setSpeakerStatusCd("A");
        st2.setSpeakerStatusDsc("A");
        SpeakerEvent se2 = new SpeakerEvent();
        se2.setSpeakerEventText("A");
        se2.setSpeakerEventDate("A");
        se2.setSpeakerEventTime("A");
        st2.setSpeakerEvent(Collections.singletonList(se2));

        var pot = new ProtectionOrderType();
        pot.setPOROrderIssueDate(Instant.now());
        pot.setOrderTypeDsc("A");
        pot.setPORConditionText("A");
        pot.setPOROrderExpiryDate(Instant.now());
        RestrainingPartyNameType rpt = new RestrainingPartyNameType();
        rpt.setRestrainingPartyName("A");
        ProtectedPartyNameType ppt = new ProtectedPartyNameType();
        ppt.setProtectedPartyName("A");
        pot.setRestrainingPartyName(Collections.singletonList(rpt));
        pot.setProtectedPartyName(Collections.singletonList(ppt));

        crl2.setProtectionOrder(Collections.singletonList(pot));

        var sat = new ScheduledAppearanceType();
        sat.setAppearanceId("A");
        sat.setCourtAgencyIdentifier("A");
        sat.setCourtRoom("A");
        sat.setAppearanceDate(Instant.now());
        sat.setAppearanceTime(Instant.now());
        sat.setAppearanceReasonCd("A");
        sat.setEstDurationHours("A");
        sat.setEstDurationMins("A");

        crl2.setScheduledAppearance(Collections.singletonList(sat));

        var hr2 = new HearingRestrictionType();
        hr2.setHearingRestrictionDate(Instant.now());
        hr2.setJudgeName("A");
        hr2.setHearingRestrictionDate(Instant.now());
        crl2.setHearingRestriction(Collections.singletonList(hr2));

        var caw = new CivilArrestWarrantType();
        caw.setWarrantDate(Instant.now());
        caw.setWarrantTypeCd("A");
        caw.setWarrantTypeDsc("A");
        crl2.setArrestWarrant(Collections.singletonList(caw));

        var ud = new UnscheduledDocumentType();
        ud.setDocumentId("A");
        ud.setFileSeqNumber("A");
        ud.setDocumentTypeDsc("A");
        FiledByType fb = new FiledByType();
        fb.setRoleTypeCode("A");
        fb.setRoleTypeCode("A");
        ud.setFiledBy(Collections.singletonList(fb));

        crl2.setUnscheduledDocument(Collections.singletonList(ud));

        var ov = new OrderToVaryType();
        ov.setAdjudicatorName("A");
        ov.setDateGranted(Instant.now());
        ov.setDocumentId("A");
        ov.setDocumentTypeDsc("A");

        crl2.setOrderToVary(Collections.singletonList(ov));

        var courtListTypes = new CourtListTypes();
        courtListTypes.setCourtLocationName("A");
        courtListTypes.setCourtRoomCode("A");
        courtListTypes.setCourtProceedingsDate(Instant.now());
        var fs = new FileSearchParameter();
        fs.setFileNumber("A");
        fs.setCourtDivisionCd("A");
        courtListTypes.setFileSearchParameter(fs);
        courtListTypes.setCivilCourtList(Collections.singletonList(crl2));
        courtListTypes.setCriminalCourtList(Collections.singletonList(crl));

        var out = new GetCrtListResponse();
        out.setCourtLists(courtListTypes);

        ResponseEntity<GetCrtListResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetCrtListResponse>>any()))
                .thenReturn(responseEntity);

        CourtController courtController = new CourtController(restTemplate, objectMapper);
        var resp = courtController.getCrtList(req);

        Assertions.assertNotNull(resp);
    }
}
