package de.timmyrs.suprdiscordbot.scripts;

import com.sun.istack.internal.NotNull;

public class FailedScript
{
	public String name;
	String hash;

	FailedScript(@NotNull String name, @NotNull String hash)
	{
		this.name = name;
		this.hash = hash;
	}
}
