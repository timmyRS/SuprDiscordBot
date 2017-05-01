package de.timmyrs.suprdiscordbot.structures;

/**
 * GatewayPayload Structure
 *
 * @author timmyRS
 */
public class GatewayPayload extends Structure
{
	/**
	 * Operation Code
	 */
	public int op;
	/**
	 * Data
	 */
	public Object d;
	/**
	 * Sequence number. Dispatch-only.
	 */
	public int s;
	/**
	 * Event name. Dispatch-only.
	 */
	public String t;

	public GatewayPayload[] getArray(int size)
	{
		return new GatewayPayload[size];
	}

	public String toString()
	{
		return "{GatewayPayload " + op + " " + d + "}";
	}

	public boolean equals(GatewayPayload o)
	{
		return o.op == this.op && o.s == this.s;
	}
}
