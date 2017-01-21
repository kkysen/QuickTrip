package io.github.kkysen.quicktrip.misc;

import java.io.IOException;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Khyber Sen
 */
@RequiredArgsConstructor
public class Misc {
    
    public static void main(final String[] args) throws IOException {
        final String price = "USD2377.17";
        System.out.println(Arrays.toString(price.split("[^0-9]+", 2)));
    }
    
}
