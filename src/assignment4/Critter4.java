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



/**
 * @author Turan Vural
 * This Critter is a biker.
 * It will not attack that of its own.
 * It will reproduce if its energy gets above start energy.
 * It will run while it can.
 * When its energy reaches 1/4 of start_energy, it stops running and reproduces.
 */
public class Critter4 extends Critter {

	private static int mileage;
	private static int totalMembers = 0;

	public Critter4() {
		totalMembers++;
	}

	/**
	 * 1. walk
	 * 2. run
	 * 3. reproduce
	 */
	@Override
	public void doTimeStep() {
		//reproduce if energy > start_energy
		if(getEnergy() > Params.start_energy && getEnergy() > Params.min_reproduce_energy) {
			Critter4 child = new Critter4();
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
		String status = "";
		String future = "";
		String past = "";
		String age = "";

		int strength = 0;
		for (Critter b : bikers) {
			strength += b.getEnergy();
		}

		if(bikers.size() == 0) {
			status = "We had a good ride. The open road goes on.";
			future = "We have no more miles in our tanks.";
			past = "We saw " + mileage + " miles of road.";
		} else {
			status = "We are " + bikers.size() + " bikers strong!";
			future = "We have " + strength + " miles in our tanks and " + totalMembers + " bikers on the road!";
			past = "We have seen " + mileage + " miles of road!";
		}

		System.out.println("This is our biker gang!");
		System.out.println(status);
		System.out.println(future);
		System.out.println(past);
	}

	@Override
	public String toString () {
		return "4";
	}
}
