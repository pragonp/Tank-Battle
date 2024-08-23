# Tank Battle

Welcome to my tank battle game prototype, developed in Java using the Processing library and Gradle as a dependency manager.

## Overview

In this game, players control tanks, strategically aiming and firing to score hits and reduce opponents' health. The player with the highest score after all levels wins. This project demonstrates my skills in game development and Java programming, turning a concept into a playable, dynamic experience.

![screenshot of the game](https://i.ibb.co/3f8DDt5/screenshot.png)

## Installation

**Step #1:** Clone the repository using:
```bash
git clone https://github.com/pragonp/Tank-Battle.git
```
and then access the directory
```bash
cd Tank-Battle
```
**Step #2:** Run the game
```bash
gradle run
```
> [!NOTE]
> If you do not have Gradle, you can follow the [Installation Guide](https://docs.gradle.org/current/userguide/installation.html)

**Step #3:** Enjoy! Some key controls are:
- Arrow keys (`UP`/`DOWN`/`LEFT`/`RIGHT`) for tank movement
- `W` and `S` to increase or decrease turret power
- `Spacebar` to fire the projectile!
- `R` to repair the tank
- `F` to add fuel
- `P` to buy more parachute
- `X` to use a larger projectile for more fun!

## Features
- **Tank Movement:** Players can navigate their tanks around the battlefield.-Aiming and Firing: Players can aim their tank's turret and fire at opponents.
- **Aiming and Firing:** Players can aim their tank's turret and fire at opponents.
- **Score Tracking:** The game keeps track of each player's score throughout the levels.
- **Health Management:** Tanks have a health system that decreases upon being hit.

## Tool and Technologies
- **Java:** The game logic is implemented in Java with OOP principles
- **Processing Libaries:** Used for rendering the game's graphics.
- **Gradle:** Employed as a dependency manager to handle library dependencies and build configurations.

## Future Enhancement
- Adding AI-controlled tanks with several difficulties
- Adding more powerups
