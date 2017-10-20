package assignment4;

import java.util.*;
/**
 * 
 * @author Brian
 * 
 * Critter1 is fat and lazy. It does not like to expend energy, so it will not move if it is not hungry. Each Critter1 has its own energy threshold. If it is above that energy
 * threshold, it will not move. If it is below that energy threshold, it has a 25% of getting off of its butt and moving one space forward. Critter1s will not change direction 
 * ever. They will move in the same direction until they die. They will take any fight (everything is possibly food after all), unless they are critically low on energy. In this 
 * case they will try to take one step backwards to escape the fight. They will not run from Algae.
 * 
 * Critter1 does not like to reproduce, as he must give away a lot of energy to do so. He will wait until he has a wealth of energy to reproduce.
 */

public class Critter1 extends Critter {

	private int energyThreshold;
	private int dir;

	public Critter1() {
		energyThreshold = (int) (1.25 * (double) Params.start_energy);
		int genVar = getRandomInt(50);
		int upDown = getRandomInt(2);
		if (upDown == 0) {
			energyThreshold = energyThreshold + genVar;
		} else {
			energyThreshold = energyThreshold - genVar;
			if(energyThreshold<0){
				energyThreshold=0;
			}
		}
		dir = getRandomInt(7);
	}

	@Override
	public void doTimeStep() {
		// Reproduce
		if ((getEnergy() > 3 * Params.start_energy) && (getEnergy() > Params.min_reproduce_energy)) {
			Critter1 child = new Critter1();
			int genVar = getRandomInt(20);
			int upDown = getRandomInt(2);
			if (upDown == 0) {
				child.energyThreshold = energyThreshold + genVar;
			} else {
				child.energyThreshold = energyThreshold - genVar;
				if(child.energyThreshold<0){
					child.energyThreshold=0;
				}
			}
			reproduce(child, getRandomInt(8));
		}

		// Walk
		if (getEnergy() >= energyThreshold) {
			return;
		} else {
			int moveChance = getRandomInt(4);
			if (moveChance == 0) {
				walk(dir);
			}
		}
	}

	@Override
	public boolean fight(String opponent) {
		if (opponent.endsWith(new Algae().toString())) {
			return true;
		} else if (getEnergy() >= energyThreshold) {
			return true;
		} else if((double)getEnergy() < (double)0.5*energyThreshold){
			walk((dir + 4) % 8);
			return false;
		}
		else{
			return true;
		}
	}

	public String toString() {
		return "1";
	}
	
	public static void runStats(java.util.List<Critter> bellies){
		int eT0t50 = 0;
		int eT51t100 = 0;
		int eT101t150 = 0;
		int eT150up = 0;
		for(Object c : bellies){
			Critter1 b = (Critter1) c;
			if(b.energyThreshold<=50){
				eT0t50 ++;
			}
			else if(b.energyThreshold<=100){
				eT51t100 ++;
			}
			else if(b.energyThreshold<=150){
				eT101t150 ++;
			}
			else {
				eT150up ++;
			}
		}
		System.out.println("There are " + bellies.size() + " Critter1s currently alive.");
		System.out.println(eT0t50 + " of them have are so lazy they don't want to move unless on the brink of death");
		System.out.println(eT51t100 + " of them are officially couch potatoes. Fun fact: I deeply relate to the critters in this catagory");
		System.out.println(eT101t150 + " of them get off their butt from time to time, but only to find more Algae snacks");
		System.out.println(eT150up + " of them are so driven by their gluttony that they constantly search for food until they are too fat to even move");
	}

	public void test(List<Critter> l) {

	}
}
