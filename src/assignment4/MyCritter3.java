package assignment4;
/* CRITTERS MyCritter3.java
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



/**
 * @author Turan Vural
 * This Critter is a Lame Lemming.
 * It will run as far as it can.
 * If it gets "tripped", it will reproduce, walk, and try to recover energy in preparation for the next big migration.
 * These Lemmings are Lame, meaning that depending on birth it will only be able to move a certain range of directions.
 * This Critter only wants to eat algae.
 */
public class MyCritter3 extends Critter {

	boolean tripped;
	int [] mobility;

	@Override
	public void doTimeStep() {
	}

	@Override
	public boolean fight(String opponent) {
		run(getRandomInt(8));
		return false;
	}

	@Override
	public String toString() {
		return "3";
	}
}
