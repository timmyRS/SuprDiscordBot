package de.timmyrs.suprdiscordbot.apis;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Internet API ('internet')
 *
 * @since 1.2
 */
public class InternetAPI
{
	/**
	 * Performs a HTTP request, returns a string.
	 *
	 * @param urlStr The URL to be (HTTP) requested.
	 * @return A string with the page's content.
	 * @throws IOException May throw an IOException, so you can handle errors yourself.
	 * @since 1.2
	 */
	public String httpString(String urlStr) throws IOException
	{
		java.net.URL url = new URL(urlStr);
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();
		InputStream in = uc.getInputStream();
		return IOUtils.toString(in, "UTF-8");
	}
}
