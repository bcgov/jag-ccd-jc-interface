package ca.bc.gov.open.jci.configuration;

import ca.bc.gov.open.jci.models.serializers.InstantDeserializer;
import ca.bc.gov.open.jci.models.serializers.InstantSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.xml.soap.SOAPMessage;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

@EnableWs
@Configuration
public class SoapConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        return restTemplate;
    }

    private MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instant.class, new InstantDeserializer());
        module.addSerializer(Instant.class, new InstantSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(SOAPMessage.WRITE_XML_DECLARATION, "true");
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setMessageProperties(props);
        messageFactory.setSoapVersion(SoapVersion.SOAP_11);
        return messageFactory;
    }

    @Bean(name = "CCD.Source.CCDUserMapping.ws:ccdUserMapping")
    public Wsdl11Definition userMappingWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/ccdUserMapping.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.CivilFileContent.ws:CivilFileContent")
    public Wsdl11Definition civilFileContentWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/civilFileContent.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.CodeValues.ws.provider:CodeValues")
    public Wsdl11Definition codeValuesWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/codeValues.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.CourtLists.ws.provider:CourtList")
    public Wsdl11Definition courtListWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/courtList.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.CriminalFileContent.ws.provider:CriminalFileContent")
    public Wsdl11Definition criminalFileContentWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/criminalFileContent.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.GetROPReport.ws:GetROPReport")
    public Wsdl11Definition getRopReportWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/getRopReport.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.GetUserLogin.WS:getUserLogin")
    public Wsdl11Definition getUserLoginWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/getUserLogin.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.ProcessResults.ws.provider:ProcessResults")
    public Wsdl11Definition processResultsWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/processResults.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.GetDocument.ws:GetDocument")
    public Wsdl11Definition getDocumentWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/getDocument.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.DevUtil.ws:DevUtils")
    public Wsdl11Definition getDevUtilsWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/getDevUtils.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CCD.Source.GetParticipantInfo.WS:getParticipantInfo")
    public Wsdl11Definition getParticipantInfo() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/getParticipantInfo.wsdl"));
        return wsdl11Definition;
    }
}
