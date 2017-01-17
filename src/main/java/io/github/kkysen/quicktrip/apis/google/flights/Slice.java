package io.github.kkysen.quicktrip.apis.google.flights;

import java.util.List;

import io.github.kkysen.quicktrip.json.Json;
import lombok.Getter;

@Json
@Getter
public class Slice {
	private int duration;
	private List<Segment> segment;
}
