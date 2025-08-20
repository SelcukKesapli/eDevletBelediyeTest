package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Plugin(name = "AllureMemory", category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE, printObject = true)
public class AllureMemoryAppender extends AbstractAppender {

    private final ThreadLocal<StringBuilder> buffer = ThreadLocal.withInitial(StringBuilder::new);

    protected AllureMemoryAppender(String name, Filter filter,
                                   Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @PluginFactory
    public static AllureMemoryAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout) {

        if (name == null) name = "AllureMemory";

        if (layout == null) {
            layout = PatternLayout.newBuilder()
                    .withPattern("[%d{HH:mm:ss}] %-5p %c{1} - %m%n")
                    .build();
        }
        return new AllureMemoryAppender(name, filter, layout, true);
    }

    @Override
    public void append(LogEvent event) {
        byte[] bytes = getLayout().toByteArray(event);
        buffer.get().append(new String(bytes, StandardCharsets.UTF_8));
    }

    public String drain() {
        String out = buffer.get().toString();
        buffer.get().setLength(0);
        return out;
    }

    public void clear() { buffer.get().setLength(0); }

    public static AllureMemoryAppender get() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        return (AllureMemoryAppender) ctx.getConfiguration().getAppender("AllureMemory");
    }
}
