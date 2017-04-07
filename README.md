# SuprDiscordBot

SuprDiscordBot is a Java Application allowing you to easily script a Discord Bot yourself in JavaScript.

## Links

- [Examples](https://github.com/timmyrs/SuprDiscordBot/tree/master/scripts/examples)
- [Documentation](https://timmyrs.github.io/SuprDiscordBot)

**Documentation Note:** What you may refer to as a "Discord Server" is called "Guild".

## Setup

1. If you don't have *at least* Java 8 installed yet, install it.
2. [Download the newest release](https://github.com/timmyrs/SuprDiscordBot/releases).
3. Unzip it.
4. Move some example scripts from `scripts/examples/` into `scripts/` so the Bot has something to do.
5. Start the `start.bat` if you're on Windows or `start.sh` if you're on Mac and Linux.

**Note when starting from console:** Make sure to `cd` into SuprDiscordBot's folder first, because something like `sh ~/SuprDiscordBot/start.sh` will simply not work.

## What Makes SuprDiscordBot's Javascript Different

- The `console` object's [functions in SuprDiscordBot](https://timmyrs.github.io/SuprDiscordBot/?de/timmyrs/suprdiscordbot/apis/ConsoleAPI.html) only accept one argument, which can be an object or an array of objects.

- `window` is not a thing. However, `window.setTimeout` can be replaced with [`script.timeout`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#timeout-java.lang.Runnable-int-).

- SuprDiscordBot built with event- and return-based-programming, which means you write all your code in anonymous functions, registered as event handlers using `script.on("eventname", function(){ ... })`. Furthermore, the [`.on`-function](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#on-java.lang.String-java.util.function.Consumer-), including many others, returns `this`, which allows you to do `script.on(...).on(...).on(...)`, etc.

- As you do not have jQuery in SuprDiscordBot, the functions [`script.each`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#each-java.lang.Object:A-java.util.function.Consumer-) and [`script.inArray`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#inArray-java.lang.Object:A-java.lang.Object-) are there to still make your coding experience easier.

- There's no `while`, as it is rather abused than used, however, `for` can be used as normal, but you should use [`script.each`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#each-java.lang.Object:A-java.util.function.Consumer-) to iterate through arrays.

## Join the Discord Guild

[![](https://discordapp.com/api/guilds/208658782966906880/embed.png?style=banner3)](https://discord.gg/MhvFkSJ)
