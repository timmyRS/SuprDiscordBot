package de.timmyrs.suprdiscordbot.apis;

import de.timmyrs.suprdiscordbot.Main;
import de.timmyrs.suprdiscordbot.scripts.Script;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Script API ('script')
 *
 * @author timmyRS
 */
@SuppressWarnings({"unused", "WeakerAccess", "SameParameterValue"})
public class ScriptAPI
{
	private Script script;

	public ScriptAPI(final Script script)
	{
		this.script = script;
	}

	/**
	 * @param event    Event name to bind
	 * @param function Consumer of object to run when event occurs
	 * @return this
	 */
	public ScriptAPI on(String event, final Consumer<Object> function)
	{
		event = event.toUpperCase();
		if(new ScriptAPI(null).inArray(new String[]{"PRESENCE_UPDATE", "USER_LEAVE"}, event))
		{
			Main.log("Script", "Event '" + event + "' is no longer being supported and thereby will not be registered.");
		}
		else
		{
			this.script.events.put(event, function);
		}
		return this;
	}

	/**
	 * Iterates through given array synchronously.
	 *
	 * @param arr      Array to iterate through
	 * @param function Anonymous function to accept each object as argument
	 * @return this
	 */
	public ScriptAPI each(final Object[] arr, final Consumer<Object> function)
	{
		for(Object o : arr)
		{
			function.accept(o);
		}
		return this;
	}

	/**
	 * @param arr    Array of objects to seek in
	 * @param object Object to seek for
	 * @return Is the given object included in the given array?
	 * @since 1.1
	 */
	public boolean inArray(final Object[] arr, final Object object)
	{
		return Arrays.asList(arr).contains(object);
	}

	/**
	 * @return Current UNIX Timestamp
	 */
	public long time()
	{
		return System.currentTimeMillis() / 1000L;
	}

	/**
	 * @return Current Time Millis
	 */
	public long timeMillis()
	{
		return System.currentTimeMillis();
	}

	/**
	 * @param min Smallest possible number
	 * @param max Biggest possible number
	 * @return Random number
	 * @since 1.1
	 */
	public int rand(int min, int max)
	{
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	/**
	 * Replacement for window.setTimeout
	 *
	 * @param function Runnable to be run after timeout
	 * @param millis   Number of millis to wait before execution of function
	 * @return Created {@link Thread} object
	 */
	public Thread timeout(final Runnable function, final int millis)
	{
		Thread t = new Thread(()->
		{
			try
			{
				Thread.sleep(millis);
				function.run();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		});
		t.start();
		return t;
	}

	/**
	 * @param event Name of the event to be fired
	 * @return this
	 */
	public ScriptAPI fireEvent(final String event)
	{
		return fireEvent(event, null);
	}

	/**
	 * @param event Name of the event to be fired
	 * @param data  Object to be given to the consumer
	 * @return this
	 */
	public ScriptAPI fireEvent(final String event, final Object data)
	{
		script.fireEvent(event, data);
		return this;
	}
}
