package io.github.kkysen.quicktrip.apis.skyscanner.hotels.prices.response;

import java.io.IOException;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import io.github.kkysen.quicktrip.apis.Json;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
public class HotelPrice {
	@SerializedName("id")
	private int mId;
	
	@SerializedName("agent_prices")
	private List<PriceElement> mPriceElements;
	/*
	class LoggingInterceptor implements Interceptor {
		  @Override public Response intercept(Interceptor.Chain chain) throws IOException {
		    Request request = chain.request();

		    long t1 = System.nanoTime();
		    logger.info(String.format("Sending request %s on %s%n%s",
		        request.url(), chain.connection(), request.headers()));

		    Response response = chain.proceed(request);

		    long t2 = System.nanoTime();
		    logger.info(String.format("Received response for %s in %.1fms%n%s",
		        response.request().url(), (t2 - t1) / 1e6d, response.headers()));

		    return response;
		  }
		}
	*/
	/*
	public static void main(String[] args) throws Exception {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("http://partners.api.skyscanner.net/apiservices/hotels/liveprices/v2/UK/EUR/en-GB/27539733/2016-12-30/2016-12-31/2/1?apiKey=prtl6749387986743898559646983194")
				.addHeader("Accept", "application/json")
				.build();
		Response response = client.newCall(request).execute();
		if(response.isSuccessful()) {
			ResponseBody body = response.body();
			System.out.println(body.string());
			body.close();
		}
	}
	*/
}
