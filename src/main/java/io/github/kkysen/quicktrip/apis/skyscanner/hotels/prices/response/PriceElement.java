package io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response;

import com.google.gson.annotations.SerializedName;

import io.github.kkysen.quicktrip.apis.Json;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
 * @author Stanley Lin
 */
@Json
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceElement {
	@SerializedName("id")
	private int mId;
	
	@SerializedName("price_total")
	private int mPriceTotal;
}
