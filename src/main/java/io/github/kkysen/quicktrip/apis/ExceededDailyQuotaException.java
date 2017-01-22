package io.github.kkysen.quicktrip.apis;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ExceededDailyQuotaException extends RuntimeException {
    
    private static final long serialVersionUID = -1596694753768276143L;
    
    private final @Getter ApiRequestException cause;
    
    public ExceededDailyQuotaException(final ApiRequestException cause, final String message) {
        super(message, cause);
        this.cause = cause;
    }
    
    public ExceededDailyQuotaException(final ApiRequestException cause) {
        this(cause, "exceeded daily quota");
    }
    
}
