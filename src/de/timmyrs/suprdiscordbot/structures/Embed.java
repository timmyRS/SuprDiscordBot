package de.timmyrs.suprdiscordbot.structures;

import de.timmyrs.suprdiscordbot.apis.DiscordAPI;

import java.util.ArrayList;

/**
 * Embed Structure.
 * You can get an empty embed structure using {@link DiscordAPI#createEmbed()}.
 * You can send an embed using {@link Channel#sendMessage(Embed)} and {@link Channel#sendMessage(String, Embed)}.
 *
 * @author timmyRS
 */
public class Embed extends Structure
{
	/**
	 * Title of embed.
	 */
	public String title;
	/**
	 * Type of embed
	 */
	public String type;
	/**
	 * Description of embed
	 */
	public String description;
	/**
	 * URL of embed
	 */
	public String url;
	/**
	 * Timestamp of embed content
	 */
	public String timestamp;
	/**
	 * Color code of the embed content
	 */
	public int color;
	public EmbedFooter footer;
	public EmbedImage image;
	public EmbedThumbnail thumbnail;
	public EmbedVideo video;
	public EmbedProvider provider;
	public EmbedAuthor author;
	public EmbedField[] fields;

	/**
	 * @param title The new embed title. Up to 256 chars.
	 * @return this
	 */
	public Embed setTitle(String title)
	{
		this.title = title;
		return this;
	}

	/**
	 * @param description The new embed description. Up to 2048 chars.
	 * @return this
	 */
	public Embed setDescription(String description)
	{
		this.description = description;
		return this;
	}

	/**
	 * @param url The new URL of embed
	 * @return this
	 */
	public Embed setURL(String url)
	{
		this.url = url;
		return this;
	}

	/**
	 * @param color HEX code of the new color
	 * @return this
	 */
	public Embed setColor(long color)
	{
		this.color = Long.decode(String.valueOf(color)).intValue();
		return this;
	}

	/**
	 * @param name     Author's name
	 * @param icon_url URL to author's icon/picture
	 * @return this
	 * @see EmbedAuthor
	 */
	public Embed setAuthor(String name, String icon_url)
	{
		return this.setAuthor(name, icon_url, null);
	}

	/**
	 * @param name     Author's name
	 * @param icon_url URL to author's icon/picture
	 * @param url      URL to author
	 * @return this
	 * @see EmbedAuthor
	 */
	public Embed setAuthor(String name, String icon_url, String url)
	{
		EmbedAuthor author = new EmbedAuthor();
		author.name = name;
		author.icon_url = icon_url;
		author.url = url;
		this.author = author;
		return this;
	}

	/**
	 * Adds an {@link EmbedField} object to {@link #fields}
	 * You can have up to 25 fields.
	 * Inline fields may not display if the thumbnail and/or image is too big.
	 *
	 * @param name   Name of the field. Up to 256 chars.
	 * @param value  Value of the field. Up to 2048 chars.
	 * @param inline Whether or not this field should display inline (true = right of the last field, false = below the last field).
	 * @return this
	 * @see EmbedField
	 */
	public Embed addField(String name, String value, boolean inline)
	{
		EmbedField field = new EmbedField();
		field.name = name;
		field.value = value;
		field.inline = inline;
		ArrayList<EmbedField> fieldsList = new ArrayList<>();
		if(fields != null)
		{
			for(EmbedField f : fields)
			{
				fieldsList.add(f);
			}
		}
		fieldsList.add(field);
		this.fields = fieldsList.toArray(new EmbedField[fieldsList.size()]);
		return this;
	}

	/**
	 * @param text Footer text. Up to 2048 chars.
	 * @return this
	 * @see EmbedFooter
	 */
	public Embed setFooter(String text)
	{
		return setFooter(text, null);
	}

	/**
	 * @param text     Footer text. Up to 2048 chars.
	 * @param icon_url URL of the footer icon
	 * @return this
	 * @see EmbedFooter
	 */
	public Embed setFooter(String text, String icon_url)
	{
		EmbedFooter footer = new EmbedFooter();
		footer.text = text;
		footer.icon_url = icon_url;
		this.footer = footer;
		return this;
	}

	public Embed[] getArray(int size)
	{
		return new Embed[size];
	}

	public String toString()
	{
		if(url != null)
		{
			return "{Embed \"" + title + "\" (" + url + ")}";
		}
		return "{Embed \"" + title + "\"}";
	}
}
