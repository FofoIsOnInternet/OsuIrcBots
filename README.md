# OsuIrcBots

A project base to build any Osu! auto-host bot in Java.

## Requirements
- JDK 8+ 
- NetBeans (Recommended) or another IDE 

## Installation
- Clone project
```
git clone git@github.com:FofoIsOnInternet/OsuIrcBots.git
```
- Go to the project folder
```
cd OsuIrcBots
```
- Set up your Osu!API and Osu!IRC credentials
```
cp .env.example .env
nano .env
```

Depending on your installation, you might need to redefine the class path to the [osu4j library](https://github.com/jamalvw/osu4j) located in `lib/osu4j-2.0.1.jar`.

## How to create a bot
- Create a new java package for your bot.
- Create a class that extends `multiplayerlobby.multiplayerBot`.
- Override methods triggered by lobby events such as `userMessage` or `allReady`.
- Instantiate your bot in the main class and run it! `bot.run()`

### Supported lobby events
| Event | Method name | Triggered on | attributes |
| --- | --- | --- | --- |
| USER_JOIN | userJoin | player joining the lobby | username, slot |
| USER_LEAVE | userLeave | player leaving the lobby | username |
| USER_MOVED | userMove | player changing slot | username, slot |
| MATCH_CLOSE | close | lobby being closed | |
| USER_MESSAGE | userMessage | player sends a message in the lobby | username, message |
| PICKED_MAP | pickedMap | player picking a map | mapid |
| GAME_START | gameStarted | game starting | |
| GAME_END | gameEnded | game ending | |
| ALL_READY | allReady | all players being ready | |
| TIMER_END | timerEnd | `!mp timer` ending | |
| USER_SCORE | userScored | player finished playing | username, score, status |
| USER_CHANGED_TEAM | userChangedTeam | player changing team | username, color |
| HOST_CHANGE | hostChanged | host changing to another player | username |

Additional events could be implemented. You may add your own or request to add one.

### Multiplayer commands
All multiplayer commands as defined by [the wiki](https://osu.ppy.sh/wiki/en/osu%21_tournament_client/osu%21tourney/Tournament_management_commands) are implemented in the class `multiplayerlobby.MultiplayerLobby`, except the following:
- `!mp make`
- `!mp makeprivate`
- `!mp settings`
- `!mp mods`
- `!mp addref`
- `!mp removeref`
- `!mp listrefs`

In your bot class you can access them via `lobby.<command>`.

You can send a message in the lobby via `lobby.say(message)`.

### Other multiplayer utility classes
| Class | Usage |
| --- | --- |
| GameMode | Lobby game mode (osu!standard, osu!mania, ...) |
| Map | Extention of `osu4j.OsuBeatmap` |
| Mod | NoFail, HardRock, FreeMod, ... |
| ScoreMode | score_v2, accuracy, ... |
| TeamColor | red, blue |
| TeamMode | Head-to-head, team-vs, ... |

### Using the Osu!API
See [osu4j librabry](https://github.com/jamalvw/osu4j) documentation.

## Example
You can find code examples in the branch [AutoHostBot](https://github.com/FofoIsOnInternet/OsuIrcBots/tree/AutoHostBot).
```
git pull origin AutoHostBot
```