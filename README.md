# EE422C-F17-HW4
*****
pair_17: Turan Vural tzv57, Brian Dubbert bpd397
https://github.com/bdoobie01/EE422C-F17-HW4

This MVC-structured program functions as follows: The Model is Critter.java, and behaves the way a game driver would. In it is fight(), step(), reproduce(), etc. methods that describe the function of the Critters that inherit from the class and the function of the simulation. The Viewer of the program is displayWorld() in Critter.java. The Controller is found in Main.main(), and handles user input and errors in the program via parsing the keywords of a command and passing them into a switch statement. Methods cleanNumbers() and getCommandRoot() were written for throwing appropriate errors in Integer parsing and getting the root word of a command, respectively. InvalidCommandException.java was created to assist in the handling of invalid input.  

Data Structures: HashMap<Integer, List<Critter>> yPop keeps track of critters in the environment according to y-coordinate. New methods mapRefresh(), mapRemove(), mapChange() were created to implement this data structure. We also used a population List<Critter> to keep track of all Critters in the environment.

* `Critter 1.java` - a.k.a. "*Big Belly*", this critter is slow and lethargic. It will move only 25% of the time if it is hungry, fight if it is hungry, and not move and fight if its energy is critically low. 
* `Critter 2.java` - This critter will reproduce if it thinks it is losing a fight. It will not fight with its own kind. It will walk in a time step but can run away from a fight.
* `Critter 3.java` - This critter is a Lemming. The directions it can move is limited and inherited from its parent. It will run until "tripped" by a fight, in which case it will reproduce and un-trip itself. Lemmings only fight Algae
* `Critter 4.java` - This critter is a biker. It will run until it has 25% energy left, in which case it will walk. It will not fight one of its own. It will reproduce if it has more than its starting energy.
