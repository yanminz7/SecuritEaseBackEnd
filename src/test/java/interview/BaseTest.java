package interview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import com.api.config.GlobalConfiguration;
/**
 * BaseTest serves as the parent class for all API test classes.
 * It provides shared setup logic, such as loading the base URL from configuration
 * and setting up logging.
 *
 * Any class that extends BaseTest will inherit this configuration.
 */
public class BaseTest {
    // Base URL for the API, loaded from config.properties via GlobalConfiguration
    protected static final String BaseUrl = GlobalConfiguration.getProperty("BaseURL");
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    /**
     * Setup method that runs once before any test methods in the class.
     * Logs the configured Base URL to help with debugging.
     * Can be extended or overridden by child test classes if needed.
     */
    @BeforeClass
    static void setup() {
        logger.info("In BaseTest.setup. Set baseURI as {}", BaseUrl);
    }
}
