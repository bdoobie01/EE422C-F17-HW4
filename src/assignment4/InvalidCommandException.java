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

    /**
     * Constructor for Exception
     * @param c invalid command string
     */
    public InvalidCommandException(String c) {
       invalidCommand = c;
    }

    /**
     * toString method
     * @return string representation of the error
     */
    public String toString() {
        return "invalid command: " + invalidCommand;
    }
}
