package io.github.kkysen.quicktrip.apis.google.flights;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import io.github.kkysen.quicktrip.json.Json;
import lombok.Getter;

@Json
@Getter
public class Trip {
	private String requestId;
	//private Data data;
	
	@SerializedName("tripOption")
	private List<Option> tripOption;
}
