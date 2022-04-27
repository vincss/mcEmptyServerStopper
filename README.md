# Minecraft Plugin : Empty Server Stopper
This plugin will stop your server after a defined amount of time when the last player leaved.

Tutorial Video :
https://youtu.be/WqvQISpr6-s

You can download the latest plugin version :
https://github.com/vincss/mcEmptyServerStopper/releases

Start the server after closing it :
https://github.com/vincss/mcsleepingserverstarter

Spigot Page : 
https://www.spigotmc.org/resources/emptyserverstopper.19409/ 


What it does :

    Wait for the last player to leave. Trigger at X minutes timers.
    After the X minutes, check if no one is connected.
        No one is connected: shutdown the server.
        Someone is connected: cancel the timer.

Install the plugin by putting the .jar file in the plugins/ directory of your server

The delay before service shutdown is configurable in : plugins/EmptyServerStopper/config.yml

(Then it can restart using mcsleepingserverstarter or MCSignOnDoor.)

-----------------

ChangeLog:

    1.0.1:
        To be compatible with older version of Minecraft Server (1.13)
    v1.0.0:
        Revert to be compatible with jdk 1.8    
    1.16.5 (Only compatible with Java 11+ https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
        Updated for latest 1.16.5 version
    1.15.1
        Updated for latest 1.15.1 version
    v0.3:
        Update to be compatible with 1.9.
    v0.2:
        New parameters :
            ShutdownTime: shutdown after x minutes when the last player left.
            ShutdownAtStart: launch the shutdown timer at start.
    v0.1:
        Working but not configurable.

Note: To create config file: first start the server, then stop it. Edit the parameters in ".\plugins\EmptyServerStopper\config.yml".
