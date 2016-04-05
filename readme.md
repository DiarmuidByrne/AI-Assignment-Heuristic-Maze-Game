# AI-Assignment-Heuristic-Maze-Game
***Diarmuid Byrne***
<br><B>Student Number:</B> G00302711
<br><B>Lecturer:</B> John Healy

## Introduction
This project was made for my final year Artificial Intelligence module. It combines heuristic and uninformed searches, along with fuzzy logic to form the gameplay elements for a maze traversing game.

## Searches and Algorithms used
- <I><B>Best First Search</I></B>
- <I><B>RandomWalk</I></B>
<BR> These algorithms were used to simulate movement within the enemies, one heuristic based and one uninformed.
The enemies randomly used one of the algorithms, with a bias towards RandomWalk for more balanced gameplay.
The enemies in game are all independantly threaded, and their movement speeds are random so that the enemies move at different intervals.
- <I><B>AStar</I></B>
<BR> When a hint item is picked up, AStar uses a heuristic (Based on the position of nearby walls and the strength of enemies in each node) to show the best path to the goal. If the key hasn't yet been found, it will display a path to the key instead.
- <I><B>Depth-Limited Depth First Search</I></B>
<BR> DLDFS was used to create a bomb object. When the object is picked up it kills all enemies in the search limit radius
- <I><B>Recursive Backtracker</I></B>
- <I><B>Eller's Maze Algorithm</I></B>
<BR> Recursive Backtracker and Eller's Maze generates the maze at the beginning of the game. Recursive Backtracker is used by default as it creates longer, winder paths with a higher river percentage and lower dead-end percentage.
- <I><B>Depth-Limited Breadth First Search</I></B>
<BR> Similar to a regular BFS but uses a limit to finish the algorithm early. Used with a radar item to show all enemies in a square radius within the limit.

## Fuzzy logic
Fuzzy logic was used to evaluate the outcome of fights for when the player comes into contact with an enemy. It uses the weapon that is currently in the player's inventory (If any exist) and the enemy's strength to return the damage dealt to the player. If the player health reaches 0, the game is over. <BR>The player's health and current weapon condition is displayed at the top of the screen using the HeadsUpDisplay class.

## Maze Architecture
A node class was made to serve as the building block of each maze. The mazes were then made using a 2-D array of Node objects. The contents of the node (Player/Enemies/Items/Walls) are set using the NodeType Enumerator.

## Issues/Bugs
- When two enemies initiate a fight at the same time, in rare occurrences it can slow down the game until the fights have been evaluated
- AStar sometimes causes the program to crash with a stackOverflow error. This only seems to happen when the player is very close to the goal.

## References
- [Eller's Maze Algorithm](http://weblog.jamisbuck.org/2010/12/29/maze-generation-eller-s-algorithm)
- [Recursive Backtracker](https://en.wikipedia.org/wiki/Maze_generation_algorithm)
- [JFuzzyLogic](http://jfuzzylogic.sourceforge.net/html/index.html)
- [Piskel Pixel Art Maker](http://www.piskelapp.com/)
