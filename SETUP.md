# SuprDiscordBot Setup

This step-by-step guide will tell you how to setup your SuprDiscordBot and it is recommended you do **exactly** as it says, to avoid any problems.

1. Open a console and verify with `java -version` that you have at least Java 8 installed - [install it](https://java.com/en/download/) if missing.
2. [Download the newest release](https://github.com/timmyrs/SuprDiscordBot/releases).
3. Unzip it.
4. Move some example scripts from `scripts/examples/` into `scripts/` so the Bot has *something* to do.
5. Double click the `start.bat` or open a terminal, `cd` into the directory and then `sh start.sh`.
6. [Register an application at Discord](https://discordapp.com/developers/applications/me/create). The only field you have to fill out is the app name.
7. Add a bot user - do not check any of the boxes - reveal and copy the **Bot Token** and **[Save changes]**.
8. Open the `config.json` in the SuprDiscordBot directory and replace `BOT_TOKEN` with your **Bot Token**.
9. Start the SuprDiscordBot again *(see step 5)*.
10. Copy the **Client ID** from your Discord Application.
11. Replace `CLIENT_ID` in the following URL with your **Client ID** and then open the URL in your browser. `https://discordapp.com/oauth2/authorize?client_id=CLIENT_ID&scope=bot&permissions=2146958463`

# That's it (Additional Information)

Now your bot should have joined your guild. You can change the profile picture of your bot by changing your **App Icon**.

If you want to Java**Script** your bot yourself these resources might be helpful:

- [What Makes SuprDiscordBot's JavaScript Different](https://github.com/timmyrs/SuprDiscordBot/blob/master/SETUP.md#what-makes-suprdiscordbots-javascript-different).
- [Examples](https://github.com/timmyrs/SuprDiscordBot/tree/master/scripts/examples)
- [Documentation](https://timmyrs.github.io/SuprDiscordBot)

**Note:** A "Discord Server" is called a "Guild".

## What Makes SuprDiscordBot's Javascript Different

- The `console` object's [functions in SuprDiscordBot](https://timmyrs.github.io/SuprDiscordBot/?de/timmyrs/suprdiscordbot/apis/ConsoleAPI.html) only accept one argument, which can be an object or an array of objects.

- `window` is not a thing. However, `window.setTimeout` can be replaced with [`script.timeout`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#timeout-java.lang.Runnable-int-).

- SuprDiscordBot built with event- and return-based-programming, which means you write all your code in anonymous functions, registered as event handlers using `script.on("eventname", function(){ ... })`. Furthermore, the [`.on`-function](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#on-java.lang.String-java.util.function.Consumer-), including many others, returns `this`, which allows you to do `script.on(...).on(...).on(...)`, etc.

- As you do not have jQuery in SuprDiscordBot, the functions [`script.each`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#each-java.lang.Object:A-java.util.function.Consumer-) and [`script.inArray`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#inArray-java.lang.Object:A-java.lang.Object-) are there to still make your coding experience easier.

- There's no `while`, as it is rather abused than used, however, `for` can be used as normal, but you should use [`script.each`](https://timmyrs.github.io/SuprDiscordBot/de/timmyrs/suprdiscordbot/apis/ScriptAPI.html#each-java.lang.Object:A-java.util.function.Consumer-) to iterate through arrays.

## Join the Discord Guild

[![](https://discordapp.com/api/guilds/208658782966906880/embed.png?style=banner3)](https://discord.gg/MhvFkSJ)
