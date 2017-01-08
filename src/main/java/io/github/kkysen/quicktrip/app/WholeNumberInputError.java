package io.github.kkysen.quicktrip.app;

import lombok.Getter;

public class WholeNumberInputError extends InputError {
    
    private static final long serialVersionUID = -8240147002987969401L;
    private final @Getter long max;
    
    public WholeNumberInputError(final String numberOfWhat, final long max) {
        super("Invalid " + numberOfWhat,
                "You must enter a whole number (less than " + max + ").");
        this.max = max;
    }
    
    public static long validate(final String input, final WholeNumberInputError error)
            throws WholeNumberInputError {
        long number = 0;
        try {
            number = Long.parseLong(input);
        } catch (final NumberFormatException e) {
            throw error;
        }
        if (number < 1 || number > error.getMax()) {
            throw error;
        }
        return number;
    }
    
    public static long validate(final String input, final String numberOfWhat,
            final long max) throws WholeNumberInputError {
        return validate(input, new WholeNumberInputError(numberOfWhat, max));
    }
    
}