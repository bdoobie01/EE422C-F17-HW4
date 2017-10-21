package assignment4;
/* CRITTERS Main.java
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

import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Method;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb; // scanner connected to keyboard input, or input file
    private static String inputFile; // input file, used instead of keyboard
    // input if specified
    static ByteArrayOutputStream testOutputString; // if test specified, holds
    // all console output
    private static String myPackage; // package of Critter file. Critter cannot
    // be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out; // if you want to restore output to
    // console

    // Gets the package name. The usage assumes that Critter and its subclasses
    // are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method. Takes input from the user and processes it in coordination with Critter.java
     *
     * @param args args can be empty. If not empty, provide two parameters -- the
     *             first is a file name, and the second is test (for test output,
     *             where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the
                    // second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output
                    // will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

		/* Do not alter the code above for your submission. */
        /* Write your code below. */

        // Prompt the user for input
        //System.out.println("Welcome to Critters!");

        boolean playing = true;
        while (playing) {
            // commands are quit, show, step, seed, make, or stats
            //System.out.println("Enter command:");
            String command = kb.nextLine();
            String[] commandParts = command.split("\\s+");
            String commandRoot = null;
            try {
                commandRoot = getCommandRoot(commandParts[0]);
            } catch (InvalidCommandException invalidCommand) {
                System.out.println(invalidCommand);
                continue;
            }

            // decide which command was given
            switch (commandRoot) {
                case "quit":
                    //command should only be "quit"
                    if (commandParts.length > 1) {
                        try {
                            throw new InvalidCommandException(command);
                        } catch (Exception e) {
                            System.out.println(e);
                            continue;
                        }
                    }
                    playing = false;
                    continue;

                case "show":
                    //command should only be "show"
                    if (commandParts.length > 1) {
                        try {
                            throw new InvalidCommandException(command);
                        } catch (Exception e) {
                            System.out.println(e);
                            continue;
                        }
                    }
                    Critter.displayWorld();
                    break;

                case "step":
                    //check for number after step
                    int steps = 1;
                    //command length at most two
                    if (commandParts.length > 2) {
                        try {
                            throw new InvalidCommandException(command);
                        } catch (InvalidCommandException e) {
                            System.out.println(e);
                            continue;
                        }
                    }
                    if (commandParts.length == 2) {
                        try {
                            steps = cleanNumbers(command, 1);
                        } catch (Exception e) {
                            System.out.println("error processing: " + command);
                            continue;
                        }
                    }
                    //if errors occur, world will take one step
                    for (int i = 0; i < steps; i++) {
                        Critter.worldTimeStep();
                    }
                    break;

                case "seed":
                    try {
                        int num = cleanNumbers(command, 1);
                        Critter.setSeed(num);
                    } catch (Exception e) {
                        System.out.println("error processing: " + command);
                    }
                    break;

                case "make":
                    //if two words, make one new object
                    if (commandParts.length == 2) {
                        try {
                            Critter.makeCritter(commandParts[1]);
                        } catch (Throwable throwable) {
                            if (throwable instanceof InvalidCritterException) {
                                System.out.println((new InvalidCommandException(command)).toString());
                            } else {
                                System.out.println("error processing: " + command);
                            }
                        }
                    } else {
                        try {
                            //if three words, make multiple of the new object
                            if (commandParts.length != 3) {
                                throw (new InvalidCommandException(command));
                            }
                            int num = cleanNumbers(command, 2);
                            for (int i = 0; i < num; i++) {
                                Critter.makeCritter(commandParts[1]);
                            }
                        } catch (Throwable throwable) {
                            if (throwable instanceof InvalidCritterException) {
                                System.out.println((new InvalidCommandException(command)).toString());
                            } else {
                                System.out.println("error processing: " + command);
                            }
                        }
                    }

                    break;

                case "stats":
                    try {
                        //get class object for critter
                        String requestedCritter = commandParts[1];
                        Class<?> c = Class.forName(myPackage + "." + requestedCritter);

                        try {
                            Critter critter = (Critter) c.getConstructor().newInstance();
                        } catch (Throwable throwable) {
                            System.err.println(throwable.toString());
                            System.out.println("error processing: " + command);
                            continue;
                        }
                        //get the list of c's instances in the world
                        List<Critter> cList = Critter.getInstances(requestedCritter);

                        try {
                            //get c's runStats
                            Method runStatsRequested = c.getMethod("runStats", List.class);
                            runStatsRequested.invoke(c, cList);
                            break;
                        } catch (Throwable throwable) {
                            //if c does not have a runStats, run Critter's runStats
                            System.err.println(throwable.toString());
                            Critter.runStats(cList);
                        }
                        //if the requestedCritter's runClass didn't run, run Critter's runClass
                        Critter.runStats(Critter.getInstances(requestedCritter));
                    } catch (Throwable throwable) {
                        System.out.println("error processing: " + command);
                        System.err.println(throwable.toString());
                    }
                    break;

                default:
                    System.out.println("error processing: " + command);
                    break;

            }

        }

		/* Write your code above */
        System.out.flush();

    }

    /**
     * This method returns the root command keyword from the command
     *
     * @return quit, show, step, seed, make, or stats
     */
    private static String getCommandRoot(String command) throws InvalidCommandException {
        if (command.contains("quit")) {
            return "quit";
        }
        if (command.contains("show")) {
            return "show";
        }
        if (command.contains("step")) {
            return "step";
        }
        if (command.contains("seed")) {
            return "seed";
        }
        if (command.contains("make")) {
            return "make";
        }
        if (command.contains("stats")) {
            return "stats";
        }

        throw new InvalidCommandException(command);
    }


    /**
     * This method takes the number field of the input and checks its validity
     *
     * @param command command string from user
     * @param index   index of integer
     * @throws InvalidCommandException
     */
    private static int cleanNumbers(String command, int index) throws InvalidCommandException {
        String[] tokens = command.split("\\s+");
        String number = tokens[index];
        int num = Integer.parseInt(number);
        double length = Math.log10(num);

        //if it is a dirty string, throw an InvalidCommandException
        if ((int) (length + 1) != number.length()) {
            throw new InvalidCommandException(command);
        } else {
            return num;
        }
    }

}
