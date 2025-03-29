package com.sakura.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Colour-coded console appender for Log4J.
 */
@SuppressWarnings("unused")
public class ANSIConsoleAppender extends ConsoleAppender {

    static Logger log = Logger.getLogger(ANSIConsoleAppender.class);

    private static final int NORMAL = 1;
    private static final int BRIGHT = 1;
    private static final int FOREGROUND_BLACK = 30;
    private static final int FOREGROUND_RED = 31;
    private static final int FOREGROUND_GREEN = 32;
    private static final int FOREGROUND_YELLOW = 33;
    private static final int FOREGROUND_BLUE = 34;
    private static final int FOREGROUND_MAGENTA = 35;
    private static final int FOREGROUND_CYAN = 36;
    private static final int FOREGROUND_WHITE = 37;

    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m] > ";
    private static final char SEPARATOR = ';';
    private static final String END_COLOUR = PREFIX + SUFFIX;

    private static final String FATAL_COLOUR = PREFIX + BRIGHT + SEPARATOR + FOREGROUND_RED + SUFFIX;
    private static final String ERROR_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_RED + SUFFIX;
    private static final String WARN_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_YELLOW + SUFFIX;
    private static final String INFO_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_GREEN + SUFFIX;
    private static final String DEBUG_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_CYAN + SUFFIX;
    private static final String TRACE_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_BLUE + SUFFIX;

    /**
     * Wraps the ANSI control characters around the output from the super-class Appender.
     */
    @Override
    public void append(LoggingEvent event) {
        this.qw.write(getColour(event.getLevel()));
        super.append(event);
        this.qw.write(END_COLOUR);

        if (this.immediateFlush) {
            this.qw.flush();
        }
    }

    /**
     * Get the appropriate control characters to change the colour for the specified logging level.
     */
    private String getColour(Level level) {
        switch (level.toInt()) {
            case Priority.FATAL_INT:
                return FATAL_COLOUR;
            case Priority.ERROR_INT:
                return ERROR_COLOUR;
            case Priority.WARN_INT:
                return WARN_COLOUR;
            case Priority.INFO_INT:
                return INFO_COLOUR;
            case Priority.DEBUG_INT:
                return DEBUG_COLOUR;
            default:
                return TRACE_COLOUR;
        }
    }

    public static void main(String[] args) {
        log.info("info");
        log.error("error");
        log.debug("debug");
    }
}
