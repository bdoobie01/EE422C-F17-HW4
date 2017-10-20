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

/**
 * @author Turan Vural
 * This Critter is a Lame Lemming.
 * It will run as far as it can.
 * If it gets "tripped", it will reproduce, walk, and try to recover energy in preparation for the next big migration.
 * These Lemmings are Lame, meaning that depending on birth it will only be able to move a certain range of directions.
 * This Critter only wants to eat algae.
 */
public class Critter3 extends Critter {

    private static Random r;

    private boolean tripped;
    private boolean[] mobility;

    public Critter3() {

        //instantiate variables
        if(r == null) {
            r = new Random();
        }
        tripped = false;
        mobility = new boolean[8];

        //set random directions
        boolean canMove = false;
        while (!canMove) {
            for (int i = 0; i < 8; i++) {
                boolean b = (r.nextInt(2) == 0);
                mobility[i] = b;
                canMove |= b;
            }
        }
    }

    public Critter3(boolean[] m) {
        if (r == null) {
            r = new Random();
        }
        tripped = false;
        mobility = m;
    }

    /**
     * 1. walk
     * 2. run
     * 3. reproduce
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

    private int getRunDirection() {
        int i = 0;
        boolean hasDirection = false;
        while (!hasDirection) {
            i = (r.nextInt(8));
            hasDirection |= mobility[i];
        }
        return  i;
    }

    @Override
    public boolean fight(String opponent) {
        run(getRunDirection());
        tripped = true;
        return opponent.equals("Algae");
    }

    @Override
    public String toString() {
        return "3";
    }
}
