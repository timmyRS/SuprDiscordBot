package de.timmyrs.suprdiscordbot.apis;

/**
 * Console API ('console')
 *
 * @author timmyRS
 */
@SuppressWarnings("unused")
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
		System.out.println("[Console]       " + o.toString());
		return this;
	}

	/**
	 * @param objects Array of objects to be logged
	 * @return this
	 */
	public ConsoleAPI log(Object[] objects)
	{
		String msg = "[";
		for(Object o : objects)
		{
			msg += o.toString() + ",";
		}
		return this.log(msg.substring(0, msg.length() - 1));
	}

	/**
	 * Replacement for console.info
	 *
	 * @param o Object to be info'd
	 * @return this
	 */
	public ConsoleAPI info(Object o)
	{
		System.out.println("[Console]       (i) " + o.toString());
		return this;
	}

	/**
	 * @param objects Array of objects to be info'd
	 * @return this
	 */
	public ConsoleAPI info(Object[] objects)
	{
		String msg = "[";
		for(Object o : objects)
		{
			msg += o.toString() + ",";
		}
		return this.log(msg.substring(0, msg.length() - 1));
	}

	/**
	 * Replacement for console.warn
	 *
	 * @param o Object to be warned
	 * @return this
	 */
	public ConsoleAPI warn(Object o)
	{
		System.out.println("[Console]       (!) " + o.toString());
		return this;
	}

	/**
	 * @param objects Array of objects to be warn'd
	 * @return this
	 */
	public ConsoleAPI warn(Object[] objects)
	{
		String msg = "[";
		for(Object o : objects)
		{
			msg += o.toString() + ",";
		}
		return this.log(msg.substring(0, msg.length() - 1));
	}

	/**
	 * Replacement for console.error
	 *
	 * @param o Object to be error'd
	 * @return this
	 */
	public ConsoleAPI error(Object o)
	{
		System.out.println("[Console]       /!\\ " + o.toString());
		return this;
	}

	/**
	 * @param objects Array of objects to be error'd
	 * @return this
	 */
	public ConsoleAPI error(Object[] objects)
	{
		String msg = "[";
		for(Object o : objects)
		{
			msg += o.toString() + ",";
		}
		return this.log(msg.substring(0, msg.length() - 1));
	}
}
