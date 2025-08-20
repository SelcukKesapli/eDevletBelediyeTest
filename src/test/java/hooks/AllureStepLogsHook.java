package hooks;

import io.cucumber.java.*;
import io.qameta.allure.Allure;
import utilities.AllureMemoryAppender;

public class AllureStepLogsHook {
    private AllureMemoryAppender mem;

    @Before
    public void beforeScenario() {
        mem = AllureMemoryAppender.get();
        if (mem != null) mem.clear();
    }

    @BeforeStep
    public void beforeStep() {
        if (mem != null) mem.clear();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (mem == null) return;
        String logs = mem.drain();
        if (!logs.isBlank()) {
            Allure.addAttachment("Log4j Logs - " + scenario.getName(),
                    "text/plain", logs, ".log");
        }
    }
}
