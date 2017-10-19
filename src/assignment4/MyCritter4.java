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

import assignment4.Critter.TestCritter;



/**
 * @author Turan Vural
 * This Critter is a biker.
 * It will not attack that of its own.
 * It will reproduce if its energy gets above start energy.
 * It will run while it can.
 * When its energy reaches 1/4 of start_energy, it stops running and reproduces.
 */
public class MyCritter4 extends TestCritter {

	private static int mileage;

	/**
	 * 1. walk
	 * 2. run
	 * 3. reproduce
	 */
	@Override
	public void doTimeStep() {
		//reproduce if energy > start_energy
		if(getEnergy() > Params.start_energy && getEnergy() > Params.min_reproduce_energy) {
			MyCritter4 child = new MyCritter4();
			reproduce(child, (getEnergy()+4)%8);
		}
		//run if energy is > 1/4 start
		if(getEnergy() > Params.run_energy_cost && getEnergy() > (Params.start_energy/4)) {
			run(getEnergy()%8);
			mileage+=2;
		}
		//walk if there is not enough energy to run
		else if (getEnergy() > Params.walk_energy_cost) {
			walk(getEnergy()%8);
			mileage+=1;
		}
	}

	@Override
	public boolean fight(String opponent) {
		//don't fight its own
		if (opponent.equals(this.getClass().getName())) {
			return false;
		}
		//don't fight if weak unless algae
		else if (getEnergy() > (Params.start_energy/4)) {
			if(opponent.equals("algae")) {
				return true;
			} else {
				return false;
			}
		}
		//otherwise fight
		return true;
	}

	public static void runStats(java.util.List<Critter> bikers) {
		int strength = 0;
		for (Critter b : bikers) {
			strength += b.getEnergy();
		}

		System.out.println("This is our biker gang!");
		System.out.println("We are " + bikers.size() + " bikers strong!");
		System.out.println("We have " + strength + " miles in our tanks!");
		System.out.println("We have seen " + mileage + " miles of road!");
	}

	@Override
	public String toString () {
		return "4";
	}
}
