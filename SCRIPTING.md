# SuprDiscordBot Scripting

**Note:** A "Discord Server" is called a "Guild".

## Debugging & Automatic Reloading

You can start the SuprDiscordBot with different arguments:

- `--debug` Debug Mode, will make SuprDiscordBot output more.

- `--live-update-scripts` Updates Scripts after you edit them. This causes the RAM to get filled up very quickly tho, so only use this whilst editing scripts.

## What Makes SuprDiscordBot's Javascript Different

- The `console` object's [functions in SuprDiscordBot](https://timmyrs.github.io/SuprDiscordBot/?de/timmyrs/suprdiscordbot/apis/ConsoleAPI.html) only accept one argument, which can be an object or an array of objects.

- `window` is not a thing. However, `window.setTimeout` can be replaced with [`script.timeout`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#timeout-java.lang.Runnable-int-).

- SuprDiscordBot built with event- and return-based-programming, which means you write all your code in anonymous functions, registered as event handlers using `script.on("eventname", function(){ ... })`. Furthermore, the [`.on`-function](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#on-java.lang.String-java.util.function.Consumer-), including many others, returns `this`, which allows you to do `script.on(...).on(...).on(...)`, etc.

- `for` and `while` can be used as normal, but you should use [`script.each`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#each-java.lang.Object:A-java.util.function.Consumer-) to iterate through arrays and [`script.inArray`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#inArray-java.lang.Object:A-java.lang.Object-) to find out if something is an array

## Other Resources

- [Examples](https://github.com/timmyrs/SuprDiscordBot/tree/master/scripts/examples)
- [Documentation](https://timmyrs.github.io/SuprDiscordBot/index.html?de/timmyrs/suprdiscordbot/structures/package-summary.html)

## Upgrading

If you plan on upgrading your version, check out the [Deprecated List](https://timmyrs.github.io/SuprDiscordBot/index.html?deprecated-list.html) for information on what functions will no longer be available and what you should replace 'em with.
