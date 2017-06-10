package de.timmyrs.suprdiscordbot.apis;

import de.timmyrs.suprdiscordbot.Main;

/**
 * Console API ('console')
 *
 * @author timmyRS
 */
@SuppressWarnings({"unused", "WeakerAccess", "SameParameterValue"})
public class ConsoleAPI
{
	/**
	 * Replacement for console.log
	 *
	 * @param o Object to be logged
	 * @return this
	 */
	public ConsoleAPI log(Object o)
	{
		Main.log("Console", o.toString());
		return this;
	}

	/**
	 * @param objects Array of objects to be logged
	 * @return this
	 */
	public ConsoleAPI log(Object[] objects)
	{
		if(objects.length == 0)
		{
			return this.log("[]");
		}
		String msg = "[";
		for(Object o : objects)
		{
			msg += o.toString() + ", ";
		}
		return this.log(msg.substring(0, msg.length() - 2) + "]");
	}

	/**
	 * Replacement for console.info
	 *
	 * @param o Object to be info'd
	 * @return this
	 */
	public ConsoleAPI info(Object o)
	{
		Main.log("Console", "(i) " + o.toString());
		return this;
	}

	/**
	 * @param objects Array of objects to be info'd
	 * @return this
	 */
	public ConsoleAPI info(Object[] objects)
	{
		if(objects.length == 0)
		{
			return this.info("[]");
		}
		String msg = "[";
		for(Object o : objects)
		{
			msg += o.toString() + ", ";
		}
		return this.log(msg.substring(0, msg.length() - 2) + "]");
	}

	/**
	 * Replacement for console.warn
	 *
	 * @param o Object to be warned
	 * @return this
	 */
	public ConsoleAPI warn(Object o)
	{
		Main.log("Console", "(!) " + o.toString());
		return this;
	}

	/**
	 * @param objects Array of objects to be warn'd
	 * @return this
	 */
	public ConsoleAPI warn(Object[] objects)
	{
		if(objects.length == 0)
		{
			return this.warn("[]");
		}
		StringBuilder msg = new StringBuilder("[");
		for(Object o : objects)
		{
			msg.append(o.toString()).append(", ");
		}
		return this.log(msg.substring(0, msg.length() - 2) + "]");
	}

	/**
	 * Replacement for console.error
	 *
	 * @param o Object to be error'd
	 * @return this
	 */
	public ConsoleAPI error(Object o)
	{
		Main.log("Console", "/!\\ " + o.toString());
		return this;
	}

	/**
	 * @param objects Array of objects to be error'd
	 * @return this
	 */
	public ConsoleAPI error(Object[] objects)
	{
		if(objects.length == 0)
		{
			return this.error("[]");
		}
		StringBuilder msg = new StringBuilder("[");
		for(Object o : objects)
		{
			msg.append(o.toString()).append(", ");
		}
		return this.log(msg.substring(0, msg.length() - 2) + "]");
	}
}
