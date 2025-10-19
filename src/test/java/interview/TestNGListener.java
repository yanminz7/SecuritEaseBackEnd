package interview;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestListener;
import org.testng.ISuiteListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
/**
 * TestNGListener is a custom TestNG listener that:
 * - Integrates ExtentReports for rich HTML test reports.
 * - Logs test lifecycle events using Log4j.
 * - Implements both ISuiteListener and ITestListener interfaces.
 */
public class TestNGListener implements  ITestListener,ISuiteListener {
    // Logger for logging test events to console or file
    private static final Logger logger = LogManager.getLogger(TestNGListener.class);
    // Shared ExtentReports instance to manage and generate the report
    private static ExtentReports extent;
    // Thread-safe ExtentTest instance for each individual test case
    private static ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();
    /**
     * Called when the entire test suite starts.
     * Initializes the ExtentReports and attaches the HTML reporter.
     */
    @Override public void onStart(ISuite suite) {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/report.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }
    /**
     * Called when the entire test suite finishes.
     * Flushes the ExtentReports to write the results to disk.
     */
    @Override public void onFinish(ISuite suite) {
        extent.flush();
    }
    /**
     * Called when an individual test method starts.
     * Logs the start and creates a new ExtentTest node.
     */
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test Started: {}", result.getName());
        String name = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(name)
                .assignCategory(result.getTestContext().getName());
        currentTest.set(test);
    }
    /**
     * Called when a test method passes.
     * Logs the success and marks the test as passed in the report.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: {}", result.getName());
        currentTest.get().pass("Passed");

    }
    /**
     * Called when a test method fails.
     * Logs the failure details and marks the test as failed in the report.
     */
    @Override
    public void onTestFailure(ITestResult result) {

        logger.error("Test Failed: {}", result.getName());
        logger.error("Exception: ", result.getThrowable());
        currentTest.get().fail(result.getThrowable());
    }

    /**
     * Called when a test method is skipped.
     * Logs the skip and marks the test as skipped in the report.
     */
    @Override public void onTestSkipped(ITestResult result){
        logger.info("Test Skipped: {}", result.getName());
        currentTest.get().skip("Skipped");
    }

}