import org.apache.logging.slf4j.Log4jMarker;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.helpers.BasicMarker;
import org.slf4j.helpers.BasicMarkerFactory;

class ManualTests {
    @Test
    void TestIt() {
        System.setProperty("log4j2.debug", "true");
        System.setProperty("log4j2.configurationFile", "classpath:jsontest.xml");
        final Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error("Some message");
        logger.info(new BasicMarkerFactory().getMarker("test"), "Marker stuffs");
        logger.warn("Some warning with exception", new Exception("Oh no"));
    }

}
