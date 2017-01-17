package io.github.kkysen.quicktrip.apis.google.flights;

import java.util.List;

import io.github.kkysen.quicktrip.json.Json;
import lombok.Getter;

@Json
@Getter
public class Trip {
	private String requestId;
	//private Data data;
	private List<Option> tripOption;
}
