package ca.bc.gov.open.jci.comparison.services;

import ca.bc.gov.open.jci.civil.GetCivilFileContent;
import ca.bc.gov.open.jci.civil.GetCivilFileContentResponse;
import ca.bc.gov.open.jci.common.code.values.GetCodeValues;
import ca.bc.gov.open.jci.common.code.values.GetCodeValuesResponse;
import ca.bc.gov.open.jci.common.criminal.file.content.GetCriminalFileContent;
import ca.bc.gov.open.jci.common.criminal.file.content.GetCriminalFileContentResponse;
import ca.bc.gov.open.jci.common.document.Document;
import ca.bc.gov.open.jci.common.document.GetDocument;
import ca.bc.gov.open.jci.common.document.GetDocumentResponse;
import ca.bc.gov.open.jci.common.rop.report.GetROPReport;
import ca.bc.gov.open.jci.common.rop.report.GetROPReportResponse;
import ca.bc.gov.open.jci.common.rop.report.Rop;
import ca.bc.gov.open.jci.comparison.config.WebServiceSenderWithAuth;
import ca.bc.gov.open.jci.court.one.GetCrtList;
import ca.bc.gov.open.jci.court.one.GetCrtListResponse;
import ca.bc.gov.open.jci.models.serializers.InstantSoapConverter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Service
public class TestService {
    private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    private final WebServiceSenderWithAuth webServiceSenderWithAuth;

    private final Javers javers = JaversBuilder.javers().build();

    @Autowired
    public TestService(WebServiceSenderWithAuth webServiceSenderWithAuth) {
        this.webServiceSenderWithAuth = webServiceSenderWithAuth;
    }

    @Value("${host.api-host}")
    private String apiHost;

    @Value("${host.wm-host}")
    private String wmHost;

    @Value("${host.username}")
    private String username;

    @Value("${host.password}")
    private String password;

    private String RAID = "83.0001";
    private String partId = RAID;
    private Instant dtm = Instant.now();

    private PrintWriter fileOutput;
    private static String outputDir = "comparison-tool/results/";

    private int overallDiff = 0;
    private int diffCounter = 0;

    enum RoomCodeCompare {
        COURT_ROOM_CODE,
        COURT_PROCEEDING_DATE,
        IDENTIFIER_CD
    }

    public void runCompares() throws Exception {
        System.out.println("INFO: CCD Diff testing started");

        // criminal file content
        getCriminalFileContentMdocCompare();
        getCriminalFileContentApprIdCompare();
        getCriminalFileContentRoomCodeCompare();

        // civil file content
        getCivilFileContentFileIdCompare();
        getCivilFileContentApprIdCompare();
        getCivilFileContentRoomCodeCompare();

        // court list
        getCourtListRoomCodeCompare();
        getCourtListFileRCompare();
        getCourtListFileNoRCompare();

        // code values
        getCodeValuesCompare();

        // document
        getDocumentCriminalCompare();
        getDocumentCivillCompare();

        // ropreport
        getRopReportCompare();
    }

    private void getCivilFileContentFileIdCompare() throws Exception {
        diffCounter = 0;

        GetCivilFileContent request = new GetCivilFileContent();

        InputStream inputIds = getClass().getResourceAsStream("/getCivilFileContentFileId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + "getCivilFileContentFileId.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getCivilFileContent with CCD File Id: " + line);
            request.setPhysicalFileId(line);
            if (!compare(new GetCriminalFileContentResponse(), request, "CivilFileContent")) {
                fileOutput.println("\nINFO: getCivilFileContent with CCD File Id: " + line);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCivilFileContentApprIdCompare() throws Exception {
        diffCounter = 0;

        GetCivilFileContent request = new GetCivilFileContent();

        InputStream inputIds = getClass().getResourceAsStream("/getCivilFileContentApprId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + " getCivilFileContentApprId.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getCivilFileContent with CCD Appearance Id: " + line);
            request.setAppearanceId(Long.parseLong(line));
            if (!compare(new GetCivilFileContentResponse(), request, "CivilFileContent")) {
                fileOutput.println("\nINFO: getCivilFileContent with CCD Appearance Id: " + line);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCivilFileContentRoomCodeCompare() throws Exception {
        diffCounter = 0;

        GetCivilFileContent request = new GetCivilFileContent();

        InputStream inputIds = getClass().getResourceAsStream("/getCivilFileContentRoomCode.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + " getCivilFileContentRoomCode.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            String roomCode = "", procDate = "", identCd = "";
            for (int i = 0; i < line.length; i++) {
                if (line[i] != null) {
                    if (RoomCodeCompare.COURT_ROOM_CODE.ordinal() == i) {
                        roomCode = line[i];
                    } else if (RoomCodeCompare.COURT_PROCEEDING_DATE.ordinal() == i) {
                        procDate = line[i];
                    } else if (RoomCodeCompare.IDENTIFIER_CD.ordinal() == i) {
                        identCd = line[i];
                    }
                }
            }

            if (!roomCode.isBlank()) {
                request.setCourtRoomCd(roomCode);
            }

            if (!procDate.isBlank()) {
                request.setCourtProceedingDate(InstantSoapConverter.parse(procDate));
            }

            if (!identCd.isBlank()) {
                request.setCourtLocaCd(identCd);
            }

            System.out.format(
                    "\nINFO: getCivilFileContent with room code : %s,  proceding date: %s, Application Code: %s \n",
                    roomCode, procDate, identCd);
            if (!compare(new GetCriminalFileContentResponse(), request, "CivilFileContent")) {
                fileOutput.format(
                        "\nINFO: getCivilFileContent with room code : %s,  proceding date: %s, Application Code: %s \n",
                        roomCode, procDate, identCd);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCriminalFileContentMdocCompare() throws Exception {
        diffCounter = 0;

        GetCriminalFileContent request = new GetCriminalFileContent();

        InputStream inputIds = getClass().getResourceAsStream("/getCriminalFileContentMdoc.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + "getCriminalFileContentMdoc.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getCriminalFileContent with CCD Mdoc: " + line);
            request.setMdocJustinNo(line);
            if (!compare(new GetCriminalFileContentResponse(), request, "CriminalFileContent")) {
                fileOutput.println("\nINFO: getCriminalFileContent with CCD Mdoc: " + line);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCriminalFileContentApprIdCompare() throws Exception {
        diffCounter = 0;

        GetCriminalFileContent request = new GetCriminalFileContent();

        InputStream inputIds = getClass().getResourceAsStream("/getCriminalFileContentApprId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + " getCriminalFileContentApprId.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getCriminalFileContent with CCD Appearance Id: " + line);
            request.setAppearanceID(line);
            if (!compare(new GetCriminalFileContentResponse(), request, "CriminalFileContent")) {
                fileOutput.println(
                        "\nINFO: getCriminalFileContent with CCD Appearance Id: " + line);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCriminalFileContentRoomCodeCompare() throws Exception {
        diffCounter = 0;

        GetCriminalFileContent request = new GetCriminalFileContent();

        InputStream inputIds =
                getClass().getResourceAsStream("/getCriminalFileContentRoomCode.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + " getCriminalFileContentRoomCode.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            String roomCode = "", procDate = "", identCd = "";
            for (int i = 0; i < line.length; i++) {
                if (line[i] != null) {
                    if (RoomCodeCompare.COURT_ROOM_CODE.ordinal() == i) {
                        roomCode = line[i];
                    } else if (RoomCodeCompare.COURT_PROCEEDING_DATE.ordinal() == i) {
                        procDate = line[i];
                    } else if (RoomCodeCompare.IDENTIFIER_CD.ordinal() == i) {
                        identCd = line[i];
                    }
                }
            }

            if (!roomCode.isBlank()) {
                request.setRoomCd(roomCode);
            }

            if (!procDate.isBlank()) {
                request.setProceedingDate(InstantSoapConverter.parse(procDate));
            }

            if (!identCd.isBlank()) {
                request.setAgencyIdentifierCd(identCd);
            }

            System.out.format(
                    "\nINFO: getCriminalFileContent with room code : %s,  proceding date: %s, identifier Code: %s \n",
                    roomCode, procDate, identCd);
            if (!compare(new GetCriminalFileContentResponse(), request, "CriminalFileContent")) {
                fileOutput.format(
                        "\nINFO: getCriminalFileContent with room code : %s,  proceding date: %s, identifier Code: %s \n",
                        roomCode, procDate, identCd);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCourtListRoomCodeCompare() throws Exception {
        diffCounter = 0;

        GetCrtList request = new GetCrtList();

        InputStream inputIds = getClass().getResourceAsStream("/getCourtListRoomCode.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput = new PrintWriter(outputDir + " getCourtListRoom.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            String roomCode = "", procDate = "", identCd = "";
            for (int i = 0; i < line.length; i++) {
                if (line[i] != null) {
                    if (RoomCodeCompare.COURT_ROOM_CODE.ordinal() == i) {
                        roomCode = line[i];
                    } else if (RoomCodeCompare.COURT_PROCEEDING_DATE.ordinal() == i) {
                        procDate = line[i];
                    } else if (RoomCodeCompare.IDENTIFIER_CD.ordinal() == i) {
                        identCd = line[i];
                    }
                }
            }

            if (!roomCode.isBlank()) {
                request.setRoomCd(roomCode);
            }

            if (!procDate.isBlank()) {
                request.setProceedingDate(InstantSoapConverter.parse(procDate));
            }

            if (!identCd.isBlank()) {
                request.setAgencyIdentifierCd(identCd);
            }

            System.out.format(
                    "\nINFO: getCourtList with room code : %s,  proceding date: %s, Identifier Code: %s \n",
                    roomCode, procDate, identCd);
            if (!compare(new GetCrtListResponse(), request, "CourtList")) {
                fileOutput.format(
                        "\nINFO: getCourtList with room code : %s,  proceding date: %s, Identifier Code: %s \n",
                        roomCode, procDate, identCd);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCourtListFileRCompare() throws Exception {
        diffCounter = 0;

        GetCrtList request = new GetCrtList();

        InputStream inputIds = getClass().getResourceAsStream("/getCourtListFileR.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput = new PrintWriter(outputDir + "getCourtListFileR.txt", StandardCharsets.UTF_8);

        request.setAgencyIdentifierCd("4681");
        request.setProceedingDate(Instant.now());
        request.setRoomCd("001");
        request.setDivisionCd("R");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(
                    "\nINFO: getCourtList with CCD division code: 'R' and File Id: " + line);
            request.setFileNumber(line);

            if (!compare(new GetCrtListResponse(), request, "CourtList")) {
                fileOutput.println("\nINFO: getCourtList with CCD File Id: " + line);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCourtListFileNoRCompare() throws Exception {
        diffCounter = 0;

        GetCrtList request = new GetCrtList();

        InputStream inputIds = getClass().getResourceAsStream("/getCourtListFileNoR.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput = new PrintWriter(outputDir + "getCourtListFileNoR.txt", StandardCharsets.UTF_8);

        request.setAgencyIdentifierCd("4681");
        request.setProceedingDate(Instant.now());
        request.setRoomCd("001");

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            String divisionCd = "", fileNumber = "";
            for (int i = 0; i < line.length; i++) {
                if (line[i] != null) {
                    if (i == 0) {
                        divisionCd = line[i];
                    } else if (i == 1) {
                        fileNumber = line[i];
                    }
                }
            }

            if (!divisionCd.isBlank()) {
                request.setDivisionCd(divisionCd);
            }

            if (!fileNumber.isBlank()) {
                request.setFileNumber(fileNumber);
            }

            System.out.format(
                    "\nINFO: getCourtListFileNo with division code : %s,  file number: %s\n",
                    divisionCd, fileNumber);

            if (!compare(new GetCrtListResponse(), request, "CourtList")) {
                fileOutput.format(
                        "\nINFO: getCourtListFileNo with division code : %s,  file number: %s\n",
                        divisionCd, fileNumber);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getCodeValuesCompare() throws Exception {
        diffCounter = 0;

        fileOutput = new PrintWriter(outputDir + "getCourtListFileNoR.txt", StandardCharsets.UTF_8);

        GetCodeValues request = new GetCodeValues();
        request.setLastRetrievedDate(ZonedDateTime.now().minusYears(20).toInstant());
        if (!compare(new GetCodeValuesResponse(), request, "CodeValues")) {
            fileOutput.println(
                    "\nINFO: getCodeValues with CCD lastRetrievedDate: "
                            + ZonedDateTime.now().minusYears(20).toInstant().toString());
            ++diffCounter;
        }

        printCompletion();
    }

    private void getDocumentCriminalCompare() throws Exception {
        diffCounter = 0;
        GetDocument request = new GetDocument();
        Document document = new Document();

        InputStream inputIds = getClass().getResourceAsStream("/getDocumentCriminal.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput = new PrintWriter(outputDir + "getDocumentCriminal.txt", StandardCharsets.UTF_8);

        document.setCourtDivisionCd("R");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.format(
                    "\nINFO: getDocumentCriminalCompare with CCD division code: %s and GUID/or Document Id: %s\n",
                    "R", line);
            document.setDocumentId(line);
            request.setDocumentRequest(document);

            if (!compare(new GetDocumentResponse(), request, "GetDocument")) {
                fileOutput.format(
                        "\nINFO: getDocumentCriminalCompare with CCD division code: %s and GUID/or Document Id: %s\n",
                        "R", line);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getDocumentCivillCompare() throws Exception {
        diffCounter = 0;
        GetDocument request = new GetDocument();
        Document document = new Document();

        InputStream inputIds = getClass().getResourceAsStream("/getDocumentCivill.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput = new PrintWriter(outputDir + "getDocumentCivill.txt", StandardCharsets.UTF_8);

        document.setCourtDivisionCd("I");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.format(
                    "\nINFO: getDocumentCivillCompare with CCD division code: %s and GUID/or Document Id: %s\n",
                    "I", line);
            document.setDocumentId(line);
            request.setDocumentRequest(document);

            if (!compare(new GetDocumentResponse(), request, "GetDocument")) {
                fileOutput.format(
                        "\nINFO: getDocumentCivillCompare with CCD division code: %s and GUID/or Document Id: %s\n",
                        "R", line);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void getRopReportCompare() throws Exception {
        diffCounter = 0;

        GetROPReport request = new GetROPReport();
        Rop rop = new Rop();
        request.setROPRequest(rop);

        InputStream inputIds = getClass().getResourceAsStream("/getRopReport.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput = new PrintWriter(outputDir + "getRopReport.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            String Param1 = "", Param2 = "", FormCd = "";
            for (int i = 0; i < line.length; i++) {
                if (line[i] != null) {
                    if (i == 0) {
                        Param1 = line[i];
                        rop.setParam1(Param1);
                    } else if (i == 1) {
                        Param2 = line[i];
                        rop.setParam2(Param2);
                    } else if (i == 2) {
                        FormCd = line[i];
                        rop.setFormCd(FormCd);
                    }
                }
            }

            System.out.format(
                    "\nINFO: getRopReport with Param1 : %s,  Param2: %s, FormCd %s\n",
                    Param1, Param1, FormCd);

            if (!compare(new GetROPReportResponse(), request, "GetROPReport")) {
                fileOutput.format(
                        "\nINFO: getRopReport with Param1 : %s,  Param2: %s, FormCd %s\n",
                        Param1, Param1, FormCd);
                ++diffCounter;
            }
        }

        printCompletion();
    }

    private void printCompletion() {
        System.out.println(
                "########################################################\n"
                        + "INFO: getFileDetailCriminal Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getFileDetailCriminal Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;

        fileOutput.close();
    }

    public <T, G> boolean compare(T response, G request, String wsdl)
            throws SOAPException, Exception {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();

        SaajSoapMessageFactory messageFactory =
                new SaajSoapMessageFactory(MessageFactory.newInstance("SOAP 1.1 Protocol"));
        messageFactory.afterPropertiesSet();

        webServiceTemplate.setMessageSender(webServiceSenderWithAuth);
        webServiceTemplate.setMessageFactory(messageFactory);

        jaxb2Marshaller.setContextPaths(
                "ca.bc.gov.open.jci.common.code.values",
                "ca.bc.gov.open.jci.common.criminal.file.content",
                "ca.bc.gov.open.jci.common.dev.utils",
                "ca.bc.gov.open.jci.common.document",
                "ca.bc.gov.open.jci.common.process.results",
                "ca.bc.gov.open.jci.common.rop.report",
                "ca.bc.gov.open.jci.common.user.login",
                "ca.bc.gov.open.jci.common.user.mapping",
                "ca.bc.gov.open.jci.civil",
                "ca.bc.gov.open.jci.court.one");

        webServiceTemplate.setMarshaller(jaxb2Marshaller);
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
        webServiceTemplate.afterPropertiesSet();

        T resultObjectWM = null;
        T resultObjectAPI = null;

        try {
            resultObjectWM = (T) webServiceTemplate.marshalSendAndReceive(wmHost + wsdl, request);
            resultObjectAPI = (T) webServiceTemplate.marshalSendAndReceive(apiHost, request);
        } catch (Exception e) {
            System.out.println("ERROR: Failed to send request... " + e);
            fileOutput.println("ERROR: Failed to send request... " + e);
        } finally {
            Thread.sleep(5000);
        }

        Diff diff = javers.compare(resultObjectAPI, resultObjectWM);

        String responseClassName = response.getClass().getName();
        if (diff.hasChanges() && printDiff(diff)) {
            return false;
        } else {
            if (resultObjectAPI == null && resultObjectWM == null) {
                System.out.println(
                        "WARN: "
                                + responseClassName.substring(
                                        responseClassName.lastIndexOf('.') + 1)
                                + ": NULL responses");
            } else {
                System.out.println(
                        "INFO: "
                                + responseClassName.substring(
                                        responseClassName.lastIndexOf('.') + 1)
                                + ": No Diff Detected");
            }
            return true;
        }
    }

    private boolean printDiff(Diff diff) {

        List<Change> actualChanges = new ArrayList<>(diff.getChanges());

        actualChanges.removeIf(
                c -> {
                    if (c instanceof ValueChange) {
                        return ((ValueChange) c).getLeft() == null
                                && ((ValueChange) c).getRight().toString().isBlank();
                    } else if (c instanceof ListChange) {
                        // we only compare value and do not want to show the array name
                        return true;
                    }

                    return false;
                });

        int diffSize = actualChanges.size();

        if (diffSize == 0) {
            return false;
        }

        String[] header = new String[] {"Property", "API Response", "WM Response"};
        String[][] table = new String[diffSize + 1][3];
        table[0] = header;

        for (int i = 0; i < diffSize; ++i) {
            Change ch = actualChanges.get(i);

            if (ch instanceof ListChange) {
                String apiVal =
                        ((ListChange) ch).getLeft() == null
                                ? "null"
                                : ((ListChange) ch).getLeft().toString();
                String wmVal =
                        ((ListChange) ch).getRight() == null
                                ? "null"
                                : ((ListChange) ch).getRight().toString();
                table[i + 1][0] = ((ListChange) ch).getPropertyNameWithPath();
                table[i + 1][1] = apiVal;
                table[i + 1][2] = wmVal;
            } else if (ch instanceof ValueChange) {
                String apiVal =
                        ((ValueChange) ch).getLeft() == null
                                ? "null"
                                : ((ValueChange) ch).getLeft().toString();
                String wmVal =
                        ((ValueChange) ch).getRight() == null
                                ? "null"
                                : ((ValueChange) ch).getRight().toString();
                table[i + 1][0] = ((ValueChange) ch).getPropertyNameWithPath();
                table[i + 1][1] = apiVal;
                table[i + 1][2] = wmVal;
            }
        }

        boolean leftJustifiedRows = false;
        int totalColumnLength = 10;
        /*
         * Calculate appropriate Length of each column by looking at width of data in
         * each column.
         *
         * Map columnLengths is <column_number, column_length>
         */
        Map<Integer, Integer> columnLengths = new HashMap<>();
        Arrays.stream(table)
                .forEach(
                        a ->
                                Stream.iterate(0, (i -> i < a.length), (i -> ++i))
                                        .forEach(
                                                i -> {
                                                    if (columnLengths.get(i) == null) {
                                                        columnLengths.put(i, 0);
                                                    }
                                                    if (columnLengths.get(i) < a[i].length()) {
                                                        columnLengths.put(i, a[i].length());
                                                    }
                                                }));

        for (Map.Entry<Integer, Integer> e : columnLengths.entrySet()) {
            totalColumnLength += e.getValue();
        }
        fileOutput.println("=".repeat(totalColumnLength));
        System.out.println("=".repeat(totalColumnLength));

        final StringBuilder formatString = new StringBuilder("");
        String flag = leftJustifiedRows ? "-" : "";
        columnLengths.entrySet().stream()
                .forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
        formatString.append("|\n");

        Stream.iterate(0, (i -> i < table.length), (i -> ++i))
                .forEach(
                        a -> {
                            fileOutput.printf(formatString.toString(), table[a]);
                            System.out.printf(formatString.toString(), table[a]);
                        });

        fileOutput.println("=".repeat(totalColumnLength));
        System.out.println("=".repeat(totalColumnLength));

        return true;
    }
}
