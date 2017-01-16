package io.github.kkysen.quicktrip.apis.google.maps.directions.response;

import io.github.kkysen.quicktrip.json.Json;
import lombok.Getter;

@Json
@Getter
public class Duration {
	private int value;
	private String text;
}
