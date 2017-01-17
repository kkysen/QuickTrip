package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.json.Json;
import lombok.Getter;

@Json
@Getter
public class Flight {
	private String carrier;
	private String number;
}
