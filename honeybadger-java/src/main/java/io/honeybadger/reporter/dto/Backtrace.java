package io.honeybadger.reporter.dto;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.honeybadger.reporter.config.ConfigContext;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing an ordered collection of backtrace elements.
 * @author <a href="https://github.com/dekobon">Elijah Zupancic</a>
 * @since 1.0.9
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Backtrace extends ArrayList<BacktraceElement>
        implements Serializable {
    private static final long serialVersionUID = 5788866962863555294L;

    private final ConfigContext config;

    /**
     * Adds each backtrace element in an error to the backtrace. The
     * config provide the required context to allow forwarding the error to HoneyBadger.
     * @param config Environment
     * @param error Error or Exception instance
     */
    public Backtrace(final ConfigContext config, final Throwable error) {
        this.config = config;

        if (error == null) {
            throw new IllegalArgumentException("Error must not be null");
        }

        addTrace(error);
    }

    /**
     * For the benefit of deserialization
     */
    @JsonCreator
    public Backtrace(@JacksonInject("config") final ConfigContext config) {
        this.config = config;
    }

    /**
     * Add an error to the collection.
     * @param error Error or Exception instance
     */
    void addTrace(final Throwable error) {
        for (StackTraceElement trace : error.getStackTrace()) {
            add(new BacktraceElement(config, trace));
        }
    }
}
