# SuprDiscordBot

[![Latest Release](https://img.shields.io/github/release/timmyrs/SuprDiscordBot/all.svg?label=Latest)](https://github.com/timmyrs/SuprDiscordBot/releases)
[![Latest Stable Release](https://img.shields.io/github/release/timmyrs/SuprDiscordBot.svg?label=Stable)](https://github.com/timmyrs/SuprDiscordBot/releases)
[![Total Downloads](https://img.shields.io/github/downloads/timmyrs/SuprDiscordBot/latest/total.svg?label=Downloads)](https://github.com/timmyrs/SuprDiscordBot/releases)
[![Github Stars](https://img.shields.io/github/stars/timmyrs/SuprDiscordBot.svg?label=Stars)](https://github.com/timmyrs/SuprDiscordBot/stargazers)
[![Floobits Status](https://floobits.com/timmyRS/SuprDiscordBot.svg)](https://floobits.com/timmyRS/SuprDiscordBot/redirect)
[![Discord Guild](https://discordapp.com/api/guilds/208658782966906880/embed.png)](https://discord.timmyrs.de)

**Note:** A "Discord Server" is called a "Guild".

## What Makes SuprDiscordBot's Javascript Different

- The `console` object's [functions in SuprDiscordBot](https://timmyrs.github.io/SuprDiscordBot/?de/timmyrs/suprdiscordbot/apis/ConsoleAPI.html) only accept one argument, which can be an object or an array of objects.

- `window` is not a thing. However, `window.setTimeout` can be replaced with [`script.timeout`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#timeout-java.lang.Runnable-int-).

- SuprDiscordBot built with event- and return-based-programming, which means you write all your code in anonymous functions, registered as event handlers using `script.on("eventname", function(){ ... })`. Furthermore, the [`.on`-function](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#on-java.lang.String-java.util.function.Consumer-), including many others, returns `this`, which allows you to do `script.on(...).on(...).on(...)`, etc.

- As you do not have jQuery in SuprDiscordBot, the functions [`script.each`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#each-java.lang.Object:A-java.util.function.Consumer-) and [`script.inArray`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#inArray-java.lang.Object:A-java.lang.Object-) are there to still make your coding experience easier.

- `for` and `while` can be used as normal, but you should use [`script.each`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#each-java.lang.Object:A-java.util.function.Consumer-) to iterate through arrays.

## Other Resources

- [Examples](https://github.com/timmyrs/SuprDiscordBot/tree/master/scripts/examples)
- [Documentation](https://timmyrs.github.io/SuprDiscordBot)

## Upgrading

If you plan on upgrading your version, check out the [Deprecated List](https://timmyrs.github.io/SuprDiscordBot/index.html?deprecated-list.html) for information on what functions will no longer be available and what you should replace 'em with.
