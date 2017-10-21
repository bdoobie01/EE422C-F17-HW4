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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */

public abstract class Critter {
	private static String myPackage;
	private static List<Critter> population = new java.util.ArrayList<Critter>();
	private static HashMap<Integer, List<Critter>> yPop = new HashMap<Integer, List<Critter>>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private Boolean hasMoved = false;
	private static Boolean inFight = false;
	private static int stepCount = 0;

	// Gets the package name. This assumes that Critter and its subclasses are
	// all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	private static java.util.Random rand = new java.util.Random();

	/**
	 * Returns a random integer
	 * @param max maximum random integer, non-inclusive
	 * @return random integer
	 */
	public static int getRandomInt(int max) {
		if (max <= 0) {
			return 0;
		}
		return rand.nextInt(max);
	}

	/**
	 * sets a new Random seed
	 * @param new_seed seed to be set
	 */
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

	/**
	 * Walk algorithm for Critter
	 * @param direction direction to walk in, 0-8
	 */
	protected final void walk(int direction) {
		energy -= Params.walk_energy_cost;
		int startX = x_coord;
		int startY = y_coord;
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
			if (inFight) {
				for (Critter cc : population) {
					if ((cc.x_coord == x_coord) && (cc.y_coord == y_coord) && (cc != this) && (cc.energy > 0)) {
						x_coord = startX;
						y_coord = startY;
					}
				}
			}

		} else {
			// System.err.println("Critter is exhausted");
		}
		// Prevent Concurre// nt Mod Error
		if (!inFight) {
			mapChange(this);
		}
	}

	/**
	 * Run algorithm for the Critter - equivalent of two walks in one step of time
	 * @param direction direction to move, 0-8
	 */
	protected final void run(int direction) {
		energy += 2 * Params.walk_energy_cost;
		energy -= Params.run_energy_cost;
		walk(direction);
		hasMoved = false;
		walk(direction);
	}

	/**
	 * Reproduction method of a Critter
	 * @param offspring Critter to be brought into the environment
	 * @param direction Direction of the new Critter to be placed in relation to its parent, 0-8
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if (getEnergy() >= Params.min_reproduce_energy) {
			offspring.x_coord = x_coord;
			offspring.y_coord = y_coord;
			Boolean nowInFight = inFight;
			inFight = false;
			offspring.walk(direction);
			inFight = nowInFight;
			offspring.energy = energy / 2;
			int x = energy % 2;
			if (x > 0) {
				energy = (energy / 2) + 1;
			} else {
				energy = energy / 2;
			}
			babies.add(offspring);
			// System.err.println(toString() + " just pooped out a baby, congrats!");
			// System.err.println("Parent energy = " + energy);
			// System.err.println("Baby energy = " + offspring.energy);
			// System.err.println("");
		}
	}

	public abstract void doTimeStep();

	public abstract boolean fight(String oponent);

	/**
	 * create and initialize a Critter subclass. critter_class_name must be the
	 * unqualified name of a concrete subclass of Critter, if not, an
	 * InvalidCritterException must be thrown. (Java weirdness: Exception
	 * throwing does not work properly if the parameter has lower-case instead
	 * of upper. For example, if craig is supplied instead of Cra// ig, an error is
	 * thrown instead of an Exception.)
	 * 
	 * @param critter_class_name name of Critter class to be made
	 * @throws InvalidCritterException thrown if invalid Critter is given
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {

			Class<?> cc = Class.forName(myPackage + "." + critter_class_name);
			Constructor<?> constructor = cc.getConstructor();
			Critter newCrit = (Critter) constructor.newInstance();
			newCrit.energy = Params.start_energy;
			newCrit.x_coord = getRandomInt(Params.world_width);
			newCrit.y_coord = getRandomInt(Params.world_height);
			population.add(newCrit); // add this critter to the population NOT
										// DONE IN CONSTRUCTOR
			mapAdd(newCrit);

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
				Class<?> crit = Class.forName(myPackage + "." + critter_class_name);
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
			mapChange(this);
		}

		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
			mapChange(this);
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
		yPop.clear();
		// Complete this method.
		// TODO complete this method
	}

	/**
	 * Step function for time across the world
	 */
	public static void worldTimeStep() {
		// Complete this method.
		// TODO Complete this method
		stepCount++;
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
				i--;

			}
		}
		for (int i = 0; i < Params.world_width; i++) {
			if (yPop.containsKey(i)) {
				List<Critter> xList = yPop.get(i);
				for (int j = 0; j < xList.size(); j++) {
					if (xList.get(j).energy <= 0) {
						xList.remove(j);
						j--;
					}
				}
			}
		}

		// Make Algae
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			Algae alg = new Algae();
			alg.setEnergy(Params.start_energy);
			mapAdd(alg);
			alg.setX_coord(getRandomInt(Params.world_width));
			alg.setY_coord(getRandomInt(Params.world_height));
			population.add(alg);
			// mapAdd(alg);
		}

		// Add babies
		for (Critter cc : babies) {
			population.add(cc);
			mapAdd(cc);
		}
		babies.clear();

		// System.err.println("");
		// System.err.println("--------------END OF STEP " + stepCount + "------------------");
		// System.err.println("");
	}

	/**
	 * Fighting algorithm for the Critters of the world
	 */
	private static void fightCritters() {
		inFight = true;
		for (List<Critter> xList : yPop.values()) {
			for (int k=0;k<xList.size();k++) {
				Critter a = xList.get(k);
				if (a.energy > 0) {
					for (int l=0;l<xList.size();l++) {
						Critter b = xList.get(l);
						if (b.energy > 0) {
							if ((a.x_coord == b.x_coord) && (a.y_coord == b.y_coord) && (a != b)) {
								// System.err.println(a.toString() + " encounters " + b.toString());
								Boolean aF = a.fight(b.toString());
								// System.err.println("aF is " + aF);
								Boolean bF = b.fight(a.toString());
								// System.err.println("bF is " + bF);
								if ((aF && bF) || ((a.x_coord == b.x_coord) && (a.y_coord == b.y_coord))) {
									// System.err.println(a.toString() + ", energy " + a.energy + " fighting "
									// + b.toString() + ", energy " + b.energy);
									int aRoll = getRandomInt(a.energy);
									if (a.toString().equals("@")) {
										aRoll = 0;
									}

									int bRoll = getRandomInt(b.energy);
									if (b.toString().equals("@")) {
										bRoll = 0;
									}

									// System.err.println("a rolls " + aRoll);
									// System.err.println("b rolls " + bRoll);

									if (aRoll >= bRoll) {
										if (b.energy < 0) {
											b.energy = 0;
										}
										a.energy += (.5) * b.energy;
										b.energy = 0;
										// System.err.println("a wins, energy is now " + a.energy + " , " + b.energy);
									} else {
										if (a.energy < 0) {
											a.energy = 0;
										}
										b.energy += (.5) * a.energy;
										a.energy = 0;
										// System.err.println("b wins, energy is now " + a.energy + " , " + b.energy);

									}
									// System.err.println("");
								}
							}
						}
					}
				}
			}
		}
		mapRefresh();
		// System.err.println("");
		inFight = false;
	}

	/**
	 * Outputs a textual representation of the world to the console
	 */
	public static void displayWorld() {

		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");

		for (int j = 0; j < Params.world_height; j++) {
			System.out.print("|");
			if (yPop.containsKey(j)) {
				for (int i = 0; i < Params.world_width; i++) {
					Boolean found = false;
					for (Critter c : yPop.get(j)) {
						if (c.x_coord == i) {
							System.out.print(c.toString());
							found = true;
							break;
						}
					}
					if (!found) {
						System.out.print(" ");
					}
				}
				System.out.println("|");
			} else {
				for (int i = 0; i < Params.world_width; i++) {
					System.out.print(" ");
				}
				System.out.println("|");
			}
		}

		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}

	/**
	 * Adds a Critter to the map
	 * @param c Critter to be added to the map
	 */
	private static void mapAdd(Critter c) {
		if (yPop.containsKey(c.y_coord)) {
			yPop.get(c.y_coord).add(c);
		} else {
			List<Critter> xList = new ArrayList<Critter>();
			xList.add(c);
			yPop.put(c.y_coord, xList);
		}
	}

	/**
	 * Updates the location of the Critter in the map data structure
	 * @param c
	 */
	private static void mapChange(Critter c) {
		mapRemove(c);
		mapAdd(c);
	}

	/**
	 * Removes a Critter from the map
	 * @param c Critter to be removed
	 */
	private static void mapRemove(Critter c) {

		for (int j = 0; j < Params.world_height; j++) {
			if (yPop.containsKey(j)) {
				List<Critter> xList = yPop.get(j);
				if (xList.contains(c)) {
					xList.remove(c);
				}
			}
		}
	}

	/**
	 * Updates the map data structure with the moved population of Critters
	 */
	private static void mapRefresh() {
		for (Critter c : population) {
			if (c.hasMoved) {
				mapChange(c);
			}
		}
	}

}
