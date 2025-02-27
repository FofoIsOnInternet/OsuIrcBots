# Osu! Auto-Host Bots

A Java library to build any Osu! auto-host bot.

## Requirements
- JDK 21
- Maven

## Installation
- Clone project
```
git@github.com:FofoIsOnInternet/osu-auto-host-bots.git
```
- Go to the project folder
```
cd osu-auto-host-bots
```
- Set up your Osu!API and Osu!IRC credentials
```
cp .env.example .env
nano .env
```
- Build package
```
mvn clean package
```

## How to create a bot
### Project setup
- Create a new Maven project
```
mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
```
- Go to your project folder
```
cd my-app
```
- Add the library in `pom.xml` 
  - Add the JitPack repository
    ```
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ```
  - Add the dependency. Replace 'TAG' with the latest version
  [![](https://jitpack.io/v/FofoIsOnInternet/osu-auto-host-bots.svg)](https://jitpack.io/#FofoIsOnInternet/osu-auto-host-bots)
    ```
    <dependency>
        <groupId>com.github.FofoIsOnInternet</groupId>
        <artifactId>osu-auto-host-bots</artifactId>
        <version>TAG</version>
    </dependency>
    ```
- Set up your Osu!API and Osu!IRC credentials in the `.env` file at the root of your project.
```
OSU_API_KEY=
OSU_IRC_SERVER=irc.ppy.sh
OSU_IRC_PORT=6667
OSU_IRC_USERNAME=
OSU_IRC_PASSWORD=
```
### Create a new bot
- Create a class that extends [`MultiplayerBot`](https://github.com/FofoIsOnInternet/osu-auto-host-bots/blob/79f79defaec0298c89899c5d8e642257d72b4dad/src/main/java/com/fofoisoninternet/multiplayerlobby/MultiplayerBot.java).
```
// YourAutoHostBot.java
package com.mycompany.app;

import com.fofoisoninternet.multiplayerlobby.MultiplayerBot;
import com.fofoisoninternet.multiplayerlobby.LobbyEvent;

public class YourAutoHostBot extends MultiplayerBot{
    public YourAutoHostBot(){
        super();
        // Rename the lobby (!mp name)
        lobby.name("AutoHost - " + this.getClass().getSimpleName());
        // Send a message
        lobby.say("Hello world!");
        // ...
    }
}
```
- Override methods triggered by lobby events such as `onUserMessage`, `onUserJoin` or `onAllUsersReady`.
```
// YourAutoHostBot.java

@Override
protected void onUserJoin(LobbyEvent event){
    lobby.say("Hello, " + event.getValue("username") + "!");
}
```
- Instantiate your bot in the main class and run it!
```
// App.java
package com.mycompany.app;

import com.mycompany.app.YourAutoHostBot;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        YourAutoHostBot bot = new YourAutoHostBot();
        bot.run();
    }
}
```
- Run your project
```
mvn exec:java -Dexec.mainClass="com.mycompany.app.App"
```

### Supported lobby events
| Event | Method name | Triggered on | attributes |
| --- | --- | --- | --- |
| USER_JOIN | onUserJoin | player joining the lobby | username, slot |
| USER_LEAVE | onUserLeave | player leaving the lobby | username |
| USER_MOVE | onUserMove | player changing slot | username, slot |
| MATCH_CLOSE | onMatchclose | lobby being closed | |
| USER_MESSAGE | onUserMessage | player sends a message in the lobby | username, message |
| MAP_PICK | onMapPick | player picking a map | mapid |
| GAME_START | onGameStart | game starting | |
| GAME_END | onGameEnd | game ending | |
| ALL_USERS_READY | onAllUsersReady | all players being ready | |
| TIMER_END | onTimerEnd | `!mp timer` ending | |
| USER_SCORE | onUserScore | player finished playing | username, score, status |
| USER_CHANGE_TEAM | onUserChangeTeam | player changing team | username, color |
| HOST_CHANGE | onHostChange | host changing to another player | username |

Additional events could be implemented. You may add your own or request to add one.

### Multiplayer commands
All multiplayer commands as defined by [the wiki](https://osu.ppy.sh/wiki/en/osu%21_tournament_client/osu%21tourney/Tournament_management_commands) are implemented in the class [`MultiplayerLobby`](https://github.com/FofoIsOnInternet/osu-auto-host-bots/blob/79f79defaec0298c89899c5d8e642257d72b4dad/src/main/java/com/fofoisoninternet/multiplayerlobby/MultiplayerLobby.java), except the following:
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
| [GameMode](https://github.com/FofoIsOnInternet/osu-auto-host-bots/blob/79f79defaec0298c89899c5d8e642257d72b4dad/src/main/java/com/fofoisoninternet/multiplayerlobby/GameMode.java) | Lobby game mode (osu!standard, osu!mania, ...) |
| [Map](https://github.com/FofoIsOnInternet/osu-auto-host-bots/blob/79f79defaec0298c89899c5d8e642257d72b4dad/src/main/java/com/fofoisoninternet/multiplayerlobby/Map.java) | Extention of `osu4j.OsuBeatmap` |
| [Mod](https://github.com/FofoIsOnInternet/osu-auto-host-bots/blob/79f79defaec0298c89899c5d8e642257d72b4dad/src/main/java/com/fofoisoninternet/multiplayerlobby/Mod.java) | NoFail, HardRock, FreeMod, ... |
| [ScoreMode](https://github.com/FofoIsOnInternet/osu-auto-host-bots/blob/79f79defaec0298c89899c5d8e642257d72b4dad/src/main/java/com/fofoisoninternet/multiplayerlobby/ScoreMode.java) | score_v2, accuracy, ... |
| [TeamColor](https://github.com/FofoIsOnInternet/osu-auto-host-bots/blob/79f79defaec0298c89899c5d8e642257d72b4dad/src/main/java/com/fofoisoninternet/multiplayerlobby/TeamColor.java) | red, blue |
| [TeamMode](https://github.com/FofoIsOnInternet/osu-auto-host-bots/blob/79f79defaec0298c89899c5d8e642257d72b4dad/src/main/java/com/fofoisoninternet/multiplayerlobby/TeamMode.java) | Head-to-head, team-vs, ... |

### Using the Osu!API
See [osu4j librabry](https://github.com/jamalvw/osu4j) documentation.