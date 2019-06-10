# SuprDiscordBot

SuprDiscordBot is a Java 8+ application allowing you to easily script a Discord Bot yourself in JavaScript.

If you encounter any bugs, have suggestions or questions, [create an issue](https://github.com/timmyrs/SuprDiscordBot/issues/new).

## Setup

1. Download SuprDiscordBot.zip [from the latest release](https://github.com/timmyrs/SuprDiscordBot/releases), unzip it and put it in a good location.
2. Start SuprDiscordBot using start.bat or start.sh.
3. Register an application on [Discord's Developer Portal](https://discordapp.com/developers/applications/).
4. Select "Bot" and click "Add Bot."
5. Reveal the token, and copy it.
6. Open the `config.json` in SuprDiscordBot's directory and replace `BOT_TOKEN` with the token you've just copied.
7. Start SuprDiscordBot again.

To get your bot to join your guild, use `https://discordapp.com/oauth2/authorize?client_id=__CLIENT_ID_HERE__&scope=bot&permissions=2146958463` where `__CLIENT_ID_HERE__` should be replaced by your app's client ID, which you can find in the "General Information" section.

## Arguments

- `--debug` Enables verbose logging, optimal for debugging scripts.

## Scripting

When making a JavaScript script for SuprDiscordBot, instead of `window` and `console`, you have [ScriptAPI](https://timmyrs.github.io/SuprDiscordBot/?de/timmyrs/suprdiscordbot/apis/ScriptAPI.html) and [ConsoleAPI](https://timmyrs.github.io/SuprDiscordBot/?de/timmyrs/suprdiscordbot/apis/ConsoleAPI.html), which you can access as `console` and  `script`, respectively.

Make sure to check out the [scripts folder](https://github.com/timmyrs/SuprDiscordBot/tree/master/scripts) for example scripts and to read the [documentation](https://timmyrs.github.io/SuprDiscordBot/index.html?de/timmyrs/suprdiscordbot/structures/package-summary.html).
