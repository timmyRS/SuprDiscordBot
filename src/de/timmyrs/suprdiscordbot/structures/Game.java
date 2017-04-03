package de.timmyrs.suprdiscordbot.structures;

/**
 * Game Structure.
 * You can get a game structure using {@link Presence#game}.
 *
 * @author timmyRS
 */
public class Game extends Structure
{
	/**
	 * The game's name
	 */
	public String name;
	/**
	 * The game type
	 * 0: Game
	 * 1: Streaming
	 */
	public int type;
	/**
	 * Stream URL. Streaming-only.
	 */
	public String url;

	public Game[] getArray(int size)
	{
		return new Game[size];
	}

	public String toString()
	{
		return "{Game \"" + this.name + "\"}";
	}
}
