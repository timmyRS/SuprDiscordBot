package de.timmyrs.suprdiscordbot.apis;

import de.timmyrs.suprdiscordbot.scripts.Script;

import java.util.function.Consumer;

/**
 * Script API ('script')
 *
 * @author timmyRS
 */
@SuppressWarnings("unused")
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
	public ScriptAPI on(final String event, final Consumer<Object> function)
	{
		this.script.events.put(event.toUpperCase(), function);
		return this;
	}

	/**
	 * @param arr      Array of objects to foreach through
	 * @param function Consumer of object to be run for each object in array
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
	 * Replacement for window.setTimeout
	 *
	 * @param function Runnable to be run after timeout
	 * @param millis   Number of millis to wait before execution of function
	 * @return this
	 */
	public ScriptAPI timeout(final Runnable function, final int millis)
	{
		new Thread(()->
		{
			try
			{
				Thread.sleep(millis);
			} catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			function.run();
		}).start();
		return this;
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
