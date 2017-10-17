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

import java.util.Scanner;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;    // scanner connected to keyboard input, or input file
    private static String inputFile;    // input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;    // if test specified, holds all console output
    private static String myPackage;    // package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;    // if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     *
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name,
     *             and the second is test (for test output, where all output to be directed to a String), or nothing.
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
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        // TODO complete main

        //Prompt the user for input
        System.out.println("Welcome to Critters!");

        Critter critter = new Critter() {
            @Override
            public void doTimeStep() {

            }

            @Override
            public boolean fight(String oponent) {
                return false;
            }
        };

        boolean playing = true;
        while (playing) {
            //commands are quit, show, step, seed, make, or stats
            String command = kb.nextLine();
            String [] commandParts = command.split("\\s+");
            String commandRoot = null;
            // TODO will this catch multiple bad inputs?
            while (commandRoot.equals(null)) {
                try {
                    commandRoot = getCommandRoot(command);
                } catch (InvalidCommandException invalidCommand) {
                    System.out.println(invalidCommand);
                    command = kb.nextLine();
                }
            }

            //decide which command was given
            switch (commandRoot) {
                // TODO does this "quit" statement play well with the others?
                case "quit" :
                    playing = false;
                    continue;

                case "show" :
                    // TODO consult with partner over implementation of this method


                    critter.displayWorld();
                    break;

                case "step" :
                    //TODO complete doTimeStep implementation
                    for (Critter c : critter.population) {

                    }
                    break;

                case "seed" :
                    break;

                case "make" :
                    break;

                case "stats" :
                    try {

                        String requestedCritter = commandParts[1];
                        Class c = Class.forName(requestedCritter);
                        Critter newCrit = (Critter) c.getConstructor().newInstance();

                        Critter statsCritter;
                        critter.runStats(); // how to pass restricted variables into these methods?

                    } catch (Exception e) {
                        if (e instanceof InvalidCommandException || e instanceof InvalidCritterException) {
                            System.out.println("error processing " + command);
                        }
                        System.out.println("ERROR UNACCOUNTED EXCEPTION");
                    }
                    break;

            }

        }

        // System.out.println("GLHF");
        
        /* Write your code above */
        System.out.flush();

    }

    /**
     * This method returns the root command keyword from the command
     *
     * @return quit, show, step, seed, make, or stats
     * @author Turan Vural
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


}
