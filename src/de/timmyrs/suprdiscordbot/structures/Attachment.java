package de.timmyrs.suprdiscordbot.structures;

/**
 * Attachment Structure.
 *
 * @author timmyRS
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Attachment extends Structure
{
	/**
	 * Attachment ID.
	 */
	public String id;
	/**
	 * Name of file attached.
	 */
	public String filename;
	/**
	 * Size of file in bytes.
	 */
	public int size;
	/**
	 * Source URL of the file.
	 */
	public String url;
	/**
	 * A proxied URl of the file.
	 */
	public String proxy_url;
	/**
	 * Height of file.
	 * Image-only.
	 */
	public int height;
	/**
	 * Width of file.
	 * Image-only.
	 */
	public int width;

	public Attachment[] getArray(int size)
	{
		return new Attachment[size];
	}

	public String toString()
	{
		return "{Attachment " + filename + " #" + id + "}";
	}
}
