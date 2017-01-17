package io.github.kkysen.quicktrip.apis.google.flights;

import io.github.kkysen.quicktrip.json.Json;
import lombok.Getter;

@Json
@Getter
public class Pricing {
	private String baseFareTotal;
	private String saleFareTotal;
	private String saleTaxTotal;
	private String saleTotal;
}
