package io.github.kkysen.quicktrip.apis.google.flights;

import java.util.List;

import io.github.kkysen.quicktrip.json.Json;
import lombok.Getter;

@Json
@Getter
public class Option {
	private String saleTotal;
	private String id;
	private List<Slice> slice;
	private List<Pricing> pricing;
	
	public int getDuration() {
		return slice.get(0).getDuration();
	}
}
