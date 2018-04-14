package org.thebubbleindex.data.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class YahooCrumbTest {

	/*
	 * Converting VBA logic from:
	 * http://www.signalsolver.com/wp-content/uploads/2017/06/EmulateURL-V1.0b. txt
	 * to Java.
	 * 
	 */

	@Test
	public void testApacheHeaderRequest() throws URISyntaxException, ParseException, IOException {

		final int maxRetry = 7;
		String crumb = null;
		String cookie = null;

		// Example:
		// ..."bkt":"finance-US-en-US-def","crumb":"wbgfBtCFUkT","device":"featurephone"...
		final String crumbSearchString = "bkt\":\"finance-US-en-US-def\",\"crumb\":\"";

		for (int i = 0; i < maxRetry; i++) {
			final HttpClient client = HttpClientBuilder.create().build();
			final HttpGet httpRequest = new HttpGet("https://finance.yahoo.com/lookup?s=rubbish");
			httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			final HttpResponse response = client.execute(httpRequest);
			final String result = EntityUtils.toString(response.getEntity());
			System.out.println(result);

			final Header[] headers = response.getAllHeaders();
			for (final Header header : headers) {
				System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
			}

			System.out.println("\nGet Response Header By Key ...\n");
			cookie = response.getFirstHeader("Set-Cookie").getValue().split(";")[0];
			System.out.println("Cookie: " + cookie);

			final int startIndexOfCrumb = result.indexOf(crumbSearchString);
			if (startIndexOfCrumb == -1) {
				throw new RuntimeException("Failed to find crumbSearchString: " + crumbSearchString);
			}

			final int crumbStartPosition = startIndexOfCrumb + crumbSearchString.length();
			final int crumbEndPosition = result.substring(crumbStartPosition).indexOf("\"") + crumbStartPosition;

			System.out.println("Crumb Start Position: " + crumbStartPosition);
			System.out.println("Crumb End Position: " + crumbEndPosition);

			crumb = result.substring(crumbStartPosition, crumbEndPosition);
			System.out.println("Crumb: " + crumb);

			if (crumb.length() == 11) {
				break;
			} else {
				crumb = null;
			}
		}

		if (crumb == null) {
			System.out.println("Failed to obtain valid crumb after " + maxRetry + " retries.");
			return;
		}

		final String stockSymbol = "TSLA";
		final String unixStartDate = "0"; // 1/1/1970
		final String unixEndDate = String.valueOf(new Date().getTime()); // Today's
																			// date
		final String urlInterval = "1d";
		final String urlEvents = "history";
		final String webRequestURL = "https://query1.finance.yahoo.com/v7/finance/download/" + stockSymbol + "?period1="
				+ unixStartDate + "&period2=" + unixEndDate + "&interval=" + urlInterval + "&events=" + urlEvents
				+ "&crumb=" + crumb;

		System.out.println("Web Request URL: " + webRequestURL);

		final HttpGet httpGet = new HttpGet(webRequestURL);
		httpGet.addHeader("Cookie", cookie);
		final HttpClient client = HttpClientBuilder.create().build();

		// Execute the request
		final HttpResponse response = client.execute(httpGet);

		final String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
		final Path path = Files.createTempFile(stockSymbol, ".csv");
		Files.write(path, result.getBytes());
	}

}