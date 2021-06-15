# Culture Bot

## What is it ?

This project is a discord bot written 100% in Kotlin to explore Gelbooru using commands. This bot is running using [Kord](https://github.com/kordlib/kord).

## Building

To build this project run:

```sh
./gradlew build
```

To generate a standalone jar application, run:

```sh
./gradlew shadowJar
```

## Running the bot

You should set the following environment variables:

``BOT_TOKEN``: The discord bot token  
``GELBOORU_API_KEY``: The Gelbooru API key  
``GELBOORU_USER_ID``: The Gelbooru user id

## Use the bot

You want to use the bot on your own server ?  
I'm hosting the bot, and you can invite it: [Invite link](https://discord.com/api/oauth2/authorize?client_id=852182377765797888&permissions=10304&scope=bot).