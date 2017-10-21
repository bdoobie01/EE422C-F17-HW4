package assignment4;
/* CRITTERS Critter3.java
 * EE422C Project 4 submission by
 * Turan Vural
 * tzv57
 * 16275
 * Brian Dubbert
 * bpd397
 * 16290
 * Slip days used: 1
 * Fall 2016
 */

import assignment4.Critter;

import java.util.Random;
import java.util.logging.Level;

/**This Critter is a Lame Lemming.
 * It will run as far as it can.
 * If it gets "tripped", it will reproduce and un-trip itself in the next turn.
 * These Lemmings are Lame, meaning that depending on birth it will only be able to move a certain range of directions.
 * This Critter only wants to eat algae. Otherwise it will always run from a fight.
 * @author Turan Vural
 */
public class Critter3 extends Critter {

    private boolean tripped;
    private boolean[] mobility;

    /**
     * Critter 3 constructor
     */
    public Critter3() {

        tripped = false;
        mobility = new boolean[8];

        //set random directions
        boolean canMove = false;
        while (!canMove) {
            for (int i = 0; i < 8; i++) {
                boolean b = (Critter.getRandomInt(2) == 0);
                mobility[i] = b;
                canMove |= b;
            }
        }
    }

    /**
     * Critter Constructor
     * @param m boolean array of valid directions
     */
    public Critter3(boolean[] m) {
        tripped = false;
        mobility = m;
    }

    /**
     * time step algorithm for Critter. Runs until it is tripped, upon which it will reproduce if it can and "un-trip" in the next turn
     */
    @Override
    public void doTimeStep() {
        if (!tripped) {
            run(getRunDirection());
        } else {

            if (getEnergy() > Params.min_reproduce_energy) {
                Critter3 littleLemmy = new Critter3(mobility);
                reproduce(littleLemmy, 8);
            } else {
                tripped = true;
            }
        }
    }

    /**
     * gets a random run direction for the Critter out of its valid run directions
     * @return direction to run
     */
    private int getRunDirection() {
        int i = 0;
        boolean hasDirection = false;
        while (!hasDirection) {
            i = (Critter.getRandomInt(mobility.length));
            hasDirection |= mobility[i];
        }
        return  i;
    }

    /**
     * Fight method for Critter 3 - this critter will not want to fight any critter other than algae
     * @param opponent String of opponent type
     * @return true for fight, false otherwise
     */
    @Override
    public boolean fight(String opponent) {
        run(getRunDirection());
        tripped = true;
        return opponent.equals("Algae");
    }

    /**
     * toString representation for Critter3
     * @return "3"
     */
    @Override
    public String toString() {
        return "3";
    }

    /**
     * Prints information about the Critter3s in the environment
     * @param lemmings list of Critter4s in the environment
     */
    public static void runStats(java.util.List<Critter> lemmings) {
        System.out.println("We are the Lemmings of this place!");
        System.out.println("We have a population of " + lemmings.size() + " lemmings!");
    }
}
