package io.github.kkysen.quicktrip.app.input;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class EmptyInputError extends InputError {
    
    private static final long serialVersionUID = -7583192816351002363L;
    
    public EmptyInputError(final String error, final String msg) {
        super(error, msg);
    }
    
}
