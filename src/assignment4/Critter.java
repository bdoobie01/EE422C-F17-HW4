package assignment4;
/* CRITTERS Critter.java
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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */

public abstract class Critter {
	private static String myPackage;
	private static List<Critter> population = new java.util.ArrayList<Critter>();
	// private static HashMap xPop = new HashMap<Integer, List<Critter>>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private Boolean hasMoved = false;

	// Gets the package name. This assumes that Critter and its subclasses are
	// all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	private static java.util.Random rand = new java.util.Random();

	public static int getRandomInt(int max) {
		if(max<=0){
			return 0;
		}
		return rand.nextInt(max);
	}

	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}

	/*
	 * a one-character long string that visually depicts your critter in the
	 * ASCII interface
	 */
	public String toString() {
		return "";
	}

	private int energy = 0;

	protected int getEnergy() {
		return energy;
	}

	private int x_coord;
	private int y_coord;

	//TODO Javadoc
	protected final void walk(int direction) {
		energy -= Params.walk_energy_cost;
		if (!hasMoved) {
			hasMoved = true;
			switch (direction) {
			case 0:
				x_coord += 1;
				if (x_coord >= Params.world_width) {
					x_coord = 0;
				}
				break;
			case 1:
				x_coord += 1;
				y_coord -= 1;
				if (x_coord >= Params.world_width) {
					x_coord = 0;
				}
				if (y_coord < 0) {
					y_coord = Params.world_height - 1;
				}
				break;
			case 2:
				y_coord -= 1;
				if (y_coord < 0) {
					y_coord = Params.world_height - 1;
				}
				break;
			case 3:
				x_coord -= 1;
				y_coord -= 1;
				if (x_coord < 0) {
					x_coord = Params.world_width - 1;
				}
				if (y_coord < 0) {
					y_coord = Params.world_height - 1;
				}
				break;
			case 4:
				x_coord -= 1;
				if (x_coord < 0) {
					x_coord = Params.world_width - 1;
				}
				break;
			case 5:
				x_coord -= 1;
				y_coord += 1;
				if (x_coord < 0) {
					x_coord = Params.world_width - 1;
				}
				if (y_coord >= Params.world_height) {
					y_coord = 0;
				}
				break;
			case 6:
				y_coord += 1;
				if (y_coord >= Params.world_height) {
					y_coord = 0;
				}
				break;
			case 7:
				x_coord += 1;
				y_coord += 1;
				if (x_coord >= Params.world_width) {
					x_coord = 0;
				}
				if (y_coord >= Params.world_height) {
					y_coord = 0;
				}

				break;
			default:
				break;
			}
		}
	}

	/**
	 * This method walks the Critter twice and adjust the Critter's energy
	 * @param direction
	 */
	protected final void run(int direction) {
		energy += 2 * Params.walk_energy_cost; //add two walk  energies, to be subtracted during walk()
		energy -= Params.run_energy_cost; //subtract the run energy
		walk(direction); //walk twice, energy is cancelled out with first line
		walk(direction);
	}

	protected final void reproduce(Critter offspring, int direction) {
		// Made by Brian

		Class<?> cn = offspring.getClass();
		try {
			Constructor<?> constructor = cn.getConstructor();
			Critter newBaby = (Critter) constructor.newInstance();
			newBaby.energy = offspring.energy / 2;

			babies.add(newBaby);
		} catch (Exception e) {
			return;
		}
	}

	public abstract void doTimeStep();

	public abstract boolean fight(String oponent);

	/**
	 * create and initialize a Critter subclass. critter_class_name must be the
	 * unqualified name of a concrete subclass of Critter, if not, an
	 * InvalidCritterException must be thrown. (Java weirdness: Exception
	 * throwing does not work properly if the parameter has lower-case instead
	 * of upper. For example, if craig is supplied instead of Craig, an error is
	 * thrown instead of an Exception.)
	 * 
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		// Made by Brianf & Turan Vural
		try {

			Class<?> cc = Class.forName("assignment4." + critter_class_name);
			Constructor<?> constructor = cc.getConstructor();
			Critter newCrit = (Critter) constructor.newInstance();
			newCrit.energy = Params.start_energy;

			Boolean empty = false;
			while (!empty) {

				newCrit.x_coord = getRandomInt(Params.world_width);
				newCrit.y_coord = getRandomInt(Params.world_height);
				Boolean isEmpty = true;
				for (Critter c : population) {
					if ((c.x_coord == newCrit.x_coord) && (c.y_coord == newCrit.y_coord)) {
						isEmpty = false;
						break;
					}
				}

				empty = isEmpty;
			}
			population.add(newCrit); // add this critter to the population NOT
										// DONE IN CONSTRUCTOR

		} catch (Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}

	}

	/**
	 * Gets a list of critters of a specific type.
	 * 
	 * @param critter_class_name
	 *            What kind of Critter is to be listed. Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try {
			for (Critter c : population) {
				Class<?> crit = Class.forName("assignment4." + critter_class_name);
				if (c.getClass().equals(crit)) {
					result.add(c);
				}
			}
		} catch (Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
		return result;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * 
	 * @param critters
	 *            List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string, 1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();
	}

	/*
	 * the TestCritter class allows some critters to "cheat". If you want to
	 * create tests of your Critter model, you can create subclasses of this
	 * class and then use the setter functions contained here.
	 * 
	 * NOTE: you must make sure that the setter functions work with your
	 * implementation of Critter. That means, if you're recording the positions
	 * of your critters using some sort of external grid or some other data
	 * structure in addition to the x_coord and y_coord functions, then you MUST
	 * update these setter functions so that they correctly update your
	 * grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}

		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}

		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}

		protected int getX_coord() {
			return super.x_coord;
		}

		protected int getY_coord() {
			return super.y_coord;
		}

		/*
		 * This method getPopulation has to be modified by you if you are not
		 * using the population ArrayList that has been provided in the starter
		 * code. In any case, it has to be implemented for grading tests to
		 * work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}

		/*
		 * This method getBabies has to be modified by you if you are not using
		 * the babies ArrayList that has been provided in the starter code. In
		 * any case, it has to be implemented for grading tests to work. Babies
		 * should be added to the general population at either the beginning OR
		 * the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		babies.clear();
		population.clear();
	}

	public static void worldTimeStep() {
		// Complete this method.

		// Reset movement tracker, deduct rest energy
		for (Critter cc : population) {
			cc.hasMoved = false;
			cc.energy -= Params.rest_energy_cost;
		}

		// doTimeStep for each critter in population
		for (Critter cc : population) {
			cc.doTimeStep();
		}

		// Process encounters
		fightCritters();

		// Clear dead critters
		for (int i = 0; i < population.size(); i++) {
			if (population.get(i).energy <= 0) {
				population.remove(i);
			}
		}
		
		//Make Algae
		for(int i =0;i<Params.refresh_algae_count;i++){
			Algae alg = new Algae();
			alg.setEnergy(Params.start_energy);
			alg.setX_coord(getRandomInt(Params.world_width-1));
			alg.setY_coord(getRandomInt(Params.world_height-1));
			population.add(alg);
		}
		
		// Add babies
				for (Critter cc : babies) {
					population.add(cc);
				}
				babies.clear();
	}

	private static void fightCritters() {
		//for not dead Critter a
		for (Critter a : population) {
			if (a.energy >= 0) {
				//for not dead critter b
				for (Critter b : population) {
					if (b.energy >= 0) {
						//if the two critters are on the same place and are not the same
						if ((a.x_coord == b.x_coord) && (a.y_coord == b.y_coord) && (a!=b)) {
							//checks if the critters want to fight
							Boolean aF = a.fight(b.toString());
							Boolean bF = b.fight(a.toString());

							//if they do want to fight
							if((aF && bF) || ((a.x_coord == b.x_coord) && (a.y_coord == b.y_coord))){

								//if Algae roll 0
								int aRoll = getRandomInt(a.energy);
								if(a.toString().equals("@")){
									aRoll = 0;
								}
								
								int bRoll = getRandomInt(b.energy);
								if(b.toString().equals("@")){
									bRoll = 0;
								}

								//checks to see who wins, a wins ties
								//adds half energy to the winning critter
								if(aRoll>=bRoll){
									if(b.energy < 0) {
										b.energy = 0;
									}
									a.energy += (.5)*b.energy;
									b.energy = 0;
								}
								else{
									if(a.energy < 0){
										a.energy = 0;
									}
									b.energy += (.5)*a.energy;
									a.energy = 0;
								}
							}
						}
					}
				}
			}
		}
	}

	public static void displayWorld() {
		// Complete this method.
		// Made by Brian

		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");

		for (int j = 0; j < Params.world_height; j++) {
			System.out.print("|");
			for (int i = 0; i < Params.world_width; i++) {
				Boolean found = false;
				for (int w = 0; w < population.size(); w++) {
					if (population.get(w).x_coord == i && population.get(w).y_coord == j && !found) {
						System.out.print(population.get(w).toString());
						found = true;
						break;
					}
				}
				if (!found) {
					System.out.print(" ");
				}
			}
			System.out.println("|");
		}

		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
}
