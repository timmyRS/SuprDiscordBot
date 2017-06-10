package de.timmyrs.suprdiscordbot;

import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Configuration Object.
 * You can get a configuration for some classes using the following methods respectively:
 * {@link de.timmyrs.suprdiscordbot.structures.Guild#getValues()} {@link de.timmyrs.suprdiscordbot.structures.Channel#getValues()} {@link de.timmyrs.suprdiscordbot.structures.User#getValues()}
 *
 * @author timmyRS
 */
@SuppressWarnings("unused")
public class Configuration
{
	private File file;
	private JsonObject json;

	Configuration(final File file)
	{
		this.file = file;
		String cont = null;
		try
		{
			if(file.exists())
			{
				cont = IOUtils.toString(new BufferedInputStream(new FileInputStream(this.file)), "UTF-8");
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		if(cont == null || cont.equals(""))
		{
			cont = "{}";
		}
		this.json = Main.jsonParser.parse(cont).getAsJsonObject();
	}

	/**
	 * @param key Key of entry to be set
	 * @param val String value to set entry to
	 * @return this
	 */
	public Configuration set(final String key, final String val)
	{
		if(this.json.has(key))
		{
			this.json.remove(key);
		}
		if(val != null)
		{
			this.json.addProperty(key, val);
		}
		return this.save();
	}

	/**
	 * @param key Key of entry to be set
	 * @param val Integer value to set entry to
	 * @return this
	 */
	public Configuration set(final String key, final int val)
	{
		if(this.json.has(key))
		{
			this.json.remove(key);
		}
		this.json.addProperty(key, val);
		return this.save();
	}

	/**
	 * @param key Key of entry to be unset
	 * @return this
	 */
	public Configuration unset(final String key)
	{
		if(this.json.has(key))
		{
			this.json.remove(key);
		}
		return this.save();
	}

	private Configuration save()
	{
		try
		{
			if(json.entrySet().size() == 0)
			{
				if(this.file.exists())
				{
					this.file.delete();
				}
			} else
			{
				if(!this.file.exists())
				{
					this.file.createNewFile();
				}
				IOUtils.write(json.toString(), new FileOutputStream(this.file), "UTF-8");
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * @param key Key of entry to get string value of
	 * @return Integer value of entry
	 */
	public String getString(final String key)
	{
		try
		{
			return this.json.get(key).getAsString();
		} catch(Exception ignored)
		{
		}
		return "";
	}

	/**
	 * @param key Key of entry to get integer value of
	 * @return Integer value of entry
	 */
	public int getInt(final String key)
	{
		final String val = getString(key);
		if(val.equals(""))
		{
			return 0;
		}
		return Integer.valueOf(val);
	}

	public boolean has(final String key)
	{
		return this.json.has(key);
	}
}
