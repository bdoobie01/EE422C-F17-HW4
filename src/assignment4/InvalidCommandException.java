package assignment4;
/* CRITTERS InvalidCommandException.java
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

public class InvalidCommandException extends Exception {
    String invalidCommand;

    public InvalidCommandException(String c) {
       invalidCommand = c;
    }

    public String toString() { return "error processing: " + invalidCommand; }
}
