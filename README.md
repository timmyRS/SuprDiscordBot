# SuprDiscordBot

SuprDiscordBot is a Java 8+ application allowing you to easily script a Discord Bot yourself in JavaScript.

If you encounter any bugs, have suggestions or questions, [create an issue](https://github.com/timmyrs/SuprDiscordBot/issues/new).

## Registering a Discord Application

1. [Register an application at Discord](https://discordapp.com/developers/applications/). The only field you have to fill out is the app name.
2. Add a bot user - do not check any of the boxes - reveal and copy the **Bot Token** and **[Save changes]**.
3. Open the `config.json` in the SuprDiscordBot directory and replace `BOT_TOKEN` with your **Bot Token**.
4. Start the SuprDiscordBot again *(see step 5)*.
5. Copy the **Client ID** from your Discord Application.
6. Replace `CLIENT_ID` in the following URL with your **Client ID** and then open it in your browser: `https://discordapp.com/oauth2/authorize?client_id=CLIENT_ID&scope=bot&permissions=2146958463`

That's it! Now your bot should have joined your guild.

You can change the profile picture of your bot by changing your **App Icon**.

## Arguments

- `--debug` Enables verbose logging, optimal for debugging scripts.

## Scripting

When making a JavaScript script for SuprDiscordBot, instead of `window` and `console`, you have [ScriptAPI](https://timmyrs.github.io/SuprDiscordBot/?de/timmyrs/suprdiscordbot/apis/ScriptAPI.html) and [ConsoleAPI](https://timmyrs.github.io/SuprDiscordBot/?de/timmyrs/suprdiscordbot/apis/ConsoleAPI.html), which you can access as `console` and  `script`, respectively.

Make sure to check out the [scripts folder](https://github.com/timmyrs/SuprDiscordBot/tree/master/scripts) for example scripts and to read the [documentation](https://timmyrs.github.io/SuprDiscordBot/index.html?de/timmyrs/suprdiscordbot/structures/package-summary.html).
