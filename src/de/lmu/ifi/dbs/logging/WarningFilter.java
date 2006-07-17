package de.lmu.ifi.dbs.logging;


/**
 * A filter for warning logs - suitable for handling warning messages.
 * A LogRecord is treated as loggable if its level is LogLevel.WARNING.
 * 
 * @author Arthur Zimek (<a href="mailto:zimek@dbs.ifi.lmu.de">zimek@dbs.ifi.lmu.de</a>)
 */
public class WarningFilter extends SelectiveFilter
{
    /**
     * Provides a filter for warning logs.
     *
     */
    public WarningFilter()
    {
        super(LogLevel.WARNING);
    }
    
}
