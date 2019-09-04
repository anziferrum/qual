package ru.anziferrum.testpay;

import ch.qos.logback.classic.util.ContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author anziferrum
 */
@SpringBootApplication (
        exclude = { SecurityAutoConfiguration.class }
)
@EnableScheduling
public class MainApp extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback.xml");

        return builder.sources(MainApp.class);
    }
}
