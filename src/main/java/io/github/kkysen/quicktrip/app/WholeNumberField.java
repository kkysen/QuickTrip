package io.github.kkysen.quicktrip.app;

import javafx.scene.control.TextField;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class WholeNumberField extends TextField {
    
    private long max;
    private final String maxString;
    
    public WholeNumberField(long max) {
        this.max = max;
        maxString = String.valueOf(max);
    }
    
    public WholeNumberField() {
        this(Long.MAX_VALUE);
    }
    
    private boolean isValid(final String text) {
        return text.matches("[0-9]*") && (text.isEmpty() || Long.parseLong(text) <= max);
    }
    
    private void checkLessThanMax() {
        final String text = getText();
        try {
            if (!text.isEmpty() && Long.parseLong(text) > max) {
                setText(maxString);
            }
        } catch (final NumberFormatException e) {
            setText(maxString);
        }
    }
    
    @Override
    public void replaceText(final int start, final int end, final String text) {
        if (isValid(text)) {
            super.replaceText(start, end, text);
            checkLessThanMax();
        }
    }
    
    @Override
    public void replaceSelection(final String replacement) {
        if (isValid(replacement)) {
            super.replaceSelection(replacement);
            checkLessThanMax();
        }
    }
    
    public long getValue() {
        return Long.parseLong(getText());
    }
    
}
