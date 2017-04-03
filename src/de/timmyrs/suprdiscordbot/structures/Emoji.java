package de.timmyrs.suprdiscordbot.structures;

public class Emoji extends Structure
{
	public String id;
	public String name;
	public int[] roles;
	public boolean require_colons;
	public boolean managed;

	public Emoji[] getArray(int size)
	{
		return new Emoji[size];
	}

	public String toString()
	{
		return "{Emoji " + this.name + " #" + this.id + "}";
	}
}
