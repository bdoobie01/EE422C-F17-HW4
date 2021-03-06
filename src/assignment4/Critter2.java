package assignment4;
/* CRITTERS Critter2.java
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

/**Critter2 is uses its reproductive system as its main defense. If it thinks it's in a losing fight, it will try to reproduce so that at least half of its energy is conserved
 * to fight for another day. If it has enough energy, it may try to reproduce twice. It will not try to fight with its own species, so it only moves half the time so that 
 * it can save its movement in case it needs to run. It will not try to reproduce otherwise. It will walk during a timestep, but if it can will always run from a fight after reproducing.
 * 
 * Critter2 moves randomly, but will try to reproduce in the direction it was moving at the time of a fight.
 * @author Brian
 *
 */
public class Critter2 extends Critter {
	private static Boolean runOnSetter = false;
	private Boolean runOn;
	private int dir;
	private static int totalFightBabies = 0;
	private int myFightBabies;
	private static int totalDoubleBirths = 0;

	/**
	 * Critter2 constructor
	 */
	public Critter2(){
		runOn = runOnSetter;
		runOnSetter = !runOnSetter;
		dir = getRandomInt(8);
		myFightBabies = 0;
	}

	/**
	 * time step algorithm for Critter. Will walk every other time step
	 */
	@Override
	public void doTimeStep() {
		dir = getRandomInt(8);
		if(runOn){
			walk(dir);
		}
		runOn = !runOn;
	}

	/**
	 * Fight algorithm for Critter2. Eats algae, will fight if it thinks it will win, will not fight another Critter2.
	 * Uses reproduction and running as escape techniques.
	 * @param opponent name of the type of Critter it is fighting
	 * @return true for fight, false otherwise
	 */
	@Override
	public boolean fight(String opponent) {
		if(opponent.toString().equals(new Algae().toString())){
			return true;
		}
		if(((double)getEnergy()>1.5*(double)Params.start_energy) && (!opponent.toString().equals(new Critter2().toString()))){
			return true;
		}
		else {
			if(getEnergy()>=Params.min_reproduce_energy){
				Critter2 child = new Critter2();
				child.dir = getRandomInt(8);
				reproduce(child, dir);
				totalFightBabies ++;
				myFightBabies ++;
			}
			if(getEnergy()>=Params.min_reproduce_energy){
				Critter2 child = new Critter2();
				child.dir = getRandomInt(8);
				reproduce(child, dir);
				totalFightBabies ++;
				myFightBabies ++;
				totalDoubleBirths++;
			}
			if(runOn){
				run(dir);
				runOn=!runOn;
			}
		}
		
		return false;
	}

	/**
	 * toString method for Critter2
	 * @return "2"
	 */
	@Override
	public String toString () {
		return "2";
	}

	/**
	 * Prints statistics of the Critter2s in the environment
	 * @param crits list of Critter2s in the environment
	 */
	public static void runStats(java.util.List<Critter> crits){
		int maxBabies = 0;
		int maxEnergy = 0;
		for(Critter c: crits){
			Critter2 cc = (Critter2)c;
			if(cc.myFightBabies>maxBabies){
				maxBabies=cc.myFightBabies;
			}
			if(cc.getEnergy()>maxEnergy){
				maxEnergy=cc.getEnergy();
			}
		}
		System.out.println("There are " + crits.size() + " total Critter2s running around");
		System.out.println("There have been a total of " + totalFightBabies + " Critter2s that were pooped out during an encounter");
		System.out.println("There have been a total of " + totalDoubleBirths + " double births!");
		System.out.println("The proud parent with the most children birthed during battle has birthed " + maxBabies + " children!");
		System.out.println("The Critter2 with the most energy has " + maxEnergy + " energy!");
	}
}
