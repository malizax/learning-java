import java.util.HashSet;       // collection of toll-free area codes
import java.util.Arrays;        // ""
import java.util.regex.Pattern; // check that phone number format is correct
import java.util.regex.Matcher; // ""
/**
 * A class to determine whether or not an incoming cell phone number is
 * toll-free or not..
 *
 * @author Desmond George
 */
public class TelephoneNumber {
    protected int areaCode, exchange, extension;    // Three parts of 10-digit
                                                    // phone number
    protected String phoneNumber;
    protected static final HashSet<Integer> tollFreeAreaCodes = new HashSet<>(
            Arrays.asList(800, 844, 855, 866, 877, 888));
    // static final means class constant
    protected static Pattern threeDigit = Pattern.compile("(\\d{3})");
    protected static Pattern fourDigit = Pattern.compile("(\\d{4})");

    /**
     * Nested within Telephone class because it is designed to be used with
     * this class only.
     */
    protected static class InvalidLengthRuntimeException
            extends RuntimeException { 
        public InvalidLengthRuntimeException(String message) {
            super(message);
        }
        public InvalidLengthRuntimeException(String message,
                Throwable throwable) {
            super(message, throwable);
        }
    }

    public TelephoneNumber(int arCd, int excg, int ext) {
        /**
         * @pre areaCode is the first three numbers of phone number
         * exchange is the following three numbers
         * extension are the final four.
         * @post construct a phone number identifying the three main components
         */
        validLength(arCd, excg, ext);
        areaCode = arCd;
        exchange = excg;
        extension = ext;
    }

    public TelephoneNumber( int excg, int ext) {
        /**
         * @pre exchange and extension given without areaCode
         * @post create phone number assuming local areaCode.
         */
        areaCode = 952;
        validLength(areaCode, excg, ext);
        exchange = excg;
        extension = ext;
    }

    public TelephoneNumber(String number) {
        /**
         * @pre phone number given as string, can be any 10 "word" character
         * ie. (letters or numbers) combination.
         * @post returns the appropriate matching 10 digit phone number.
         */
        if(number.matches("(.*)[a-zA-Z](.*)")) {
            // Accepts zero or more of any character (.*) before or after
            // the letter it finds
            number = number.replaceAll("[a-cA-C]", "2");
            number = number.replaceAll("[d-fD-F]", "3");
            number = number.replaceAll("[g-iG-I]", "4");
            number = number.replaceAll("[j-lJ-L]", "5");
            number = number.replaceAll("[m-oM-O]", "6");
            number = number.replaceAll("[p-sP-S]", "7");
            number = number.replaceAll("[t-uT-U]", "8");
            number = number.replaceAll("[w-zW-Z]", "9");
        }
        if(number.matches("(.*)[)(-.](.*)")) {
            // Accepts zero or more of any characters before or after the
            // seperator that it finds
            number = number.replaceAll("[)(]", "");
            String[] parts = number.split("[)-.]");
            areaCode = Integer.parseInt(parts[0]);
            exchange = Integer.parseInt(parts[1]);
            extension = Integer.parseInt(parts[2]);
        } else {
            // Convert the string of numbers into actual integer values
            areaCode = Integer.parseInt(number.substring(0, 3));
            exchange = Integer.parseInt(number.substring(3, 6));
            extension = Integer.parseInt(number.substring(6));
        }
        validLength(areaCode, exchange, extension);
        phoneNumber = number;
    }

    public void checkTollFree() {
        /**
         * @pre areaCode is the identified first three digits of phoneNumber
         * @post checks to see if there is a match between areaCode and the 
         * identified toll-free HashSet
         */
        if(tollFreeAreaCodes.contains(this.areaCode)) {
            System.out.println("Telephone number (" + areaCode + ") " +
                    exchange + "." + extension +
                    " Identified as toll-free number!");
        } else {
            System.out.println("Telephone number (" + areaCode + ") " + 
                exchange + "." + extension + " Identified as regular number.");
        }
    }

    public void validLength(int arCd, int excg, int ext) throws
        InvalidLengthRuntimeException {
        /**
         * @post determines whether or not code lengths are valid, throws
         * RuntimeException if it is not
         */
        Matcher areaCode = threeDigit.matcher(Integer.toString(arCd));
        Matcher exchange = threeDigit.matcher(Integer.toString(excg));
        Matcher extension = fourDigit.matcher(Integer.toString(ext));
        if(!areaCode.matches()) {
            throw new InvalidLengthRuntimeException("Invalid area code: " +
                    arCd + "; length must be 3 characters long");
        }
        if(!exchange.matches()) {
            throw new InvalidLengthRuntimeException("Invalid exchange code: " +
                    excg + "; length must be 3 characters long");
        }
        if(!extension.matches()) {
            throw new InvalidLengthRuntimeException("Invalid extension code: "+
                    ext + "; length must be 4 characters long");
        }
    }

   public static void main(String[] args) {
       /**
        * various test cases
        */
       TelephoneNumber t = new TelephoneNumber(210, 4180);
       t.checkTollFree();
       t = new TelephoneNumber(888, 648, 7831);
       t.checkTollFree();
       t = new TelephoneNumber("(651)-BAT.TERY");
       t.checkTollFree();
       t = new TelephoneNumber("95436478613");
       t.checkTollFree();
       t = new TelephoneNumber(4568, 15689, 123);
       t.checkTollFree();
   }
}
