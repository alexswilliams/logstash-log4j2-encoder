import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.helpers.BasicMarkerFactory;

class ManualTests {
    @Test
    void TestIt() {
        System.setProperty("log4j2.debug", "true");
        System.setProperty("log4j2.configurationFile", "classpath:json-test.xml");
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        MDC.put("SomeKey", "SomeValue");
        MDC.put("OtherKey", "OtherValue");
        MDC.put("MyKey", "MyValue");
        logger.error("Some message");
        MDC.clear();

        logger.info(new BasicMarkerFactory().getMarker("test"), "Marker stuffs");
        logger.warn("Some warning with exception", new Exception("Oh no"));
    }

}
