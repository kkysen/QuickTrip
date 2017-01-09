package io.github.kkysen.quicktrip.app;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class WholeNumberField extends TextField {
    
    private long max;
    private String maxString;
    
    @Getter
    private ObjectProperty<Long> pMax;
    
    @SuppressWarnings("unchecked")
	public WholeNumberField(long max) {
        this.max = max;
        maxString = String.valueOf(max);
        
        //For fxgraph support
        /*try {
			pMax = JavaBeanObjectPropertyBuilder.create()
					.bean(pMax)
					.name("max")
					.getter(WholeNumberField.class.getMethod("getPMax"))
					.setter(WholeNumberField.class.getMethod("setPMax", Long.class))
					.build();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}*/
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
    
    public void setPMax(Long Max) {
    	System.out.println("IT WORKS");
    }
    
    public void setMax(long max) {
    	this.max = max;
    	maxString = String.valueOf(max);
    }
    
}
