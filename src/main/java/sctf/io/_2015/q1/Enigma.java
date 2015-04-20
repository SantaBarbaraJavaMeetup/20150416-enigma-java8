package sctf.io._2015.q1;

import java.util.*;

/**
 * <p>
 * Title: Enigma
 * </p>
 * <p>
 * Description: A Simulation of the German Military Enigma Machine.
 * Specifications of rotors and reflectors obtained from
 * http://www.codesandciphers.org.uk/enigma/rotorspec.htm and
 * http://homepages.tesco.net/~andycarlson/enigma/simulating_enigma.html
 * </p>
 * 
 * @author Meghan Emilio
 * @version 1.0
 */
public class Enigma {

    // STATIC ROTORS
    public final static StringBuffer rotorI = new StringBuffer(
            "EKMFLGDQVZNTOWYHXUSPAIBRCJ");
    public final static StringBuffer rotorII = new StringBuffer(
            "AJDKSIRUXBLHWTMCQGZNPYFVOE");
    public final static StringBuffer rotorIII = new StringBuffer(
            "BDFHJLCPRTXVZNYEIWGAKMUSQO");
    public final static StringBuffer rotorIV = new StringBuffer(
            "ESOVPZJAYQUIRHXLNFTGKDCMWB");
    public final static StringBuffer rotorV = new StringBuffer(
            "VZBRGITYUPSDNHLXAWMJQOFECK");
    public final static StringBuffer rotorVI = new StringBuffer(
            "JPGVOUMFYQBENHZRDKASXLICTW");
    public final static StringBuffer rotorVII = new StringBuffer(
            "NZJHGRCXMYSWBOUFAIVLPEKQDT");
    public final static StringBuffer rotorVIII = new StringBuffer(
            "JPGVOUMFYQBENHZRDKASXLICTW");

    // STATIC REFLECTORS
    public static final StringBuffer reflectorB = new StringBuffer(
            "YRUHQSLDPXNGOKMIEBFZCWVJAT");
    public static final StringBuffer reflectorC = new StringBuffer(
            "FVPJIAOYEDRZXWGCTKUQSBNMHL");
    public static final StringBuffer reflector0 = new StringBuffer(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    private static final int IFromCh(int c) {
        return c - 'A';
    }

    private static final int ChFromI(int c) {
        return c + 'A';
    }

    public static final class Rotor {
        // f u javascript enigma
        public final String wires;
        public final int notch;
        public final int[] map = new int[26];
        public final int[] mapRev = new int[26];

        public Rotor(StringBuffer legacyRotor, StringBuffer legacyNotch) {
            wires = legacyRotor.toString();
            notch = legacyNotch.length() == 0 ? 0 : IFromCh(legacyNotch
                    .toString().charAt(0));
            for (int iFrom = 0; iFrom < 26; iFrom++) {
                int iTo = IFromCh(wires.charAt(iFrom));
                map[iFrom] = (26 + iTo - iFrom) % 26;
                mapRev[iTo] = (26 + iFrom - iTo) % 26;
            }
        }
    }

    // STATIC "NOTCHES" - when each rotor rotates
    public final static StringBuffer[] notches = { new StringBuffer("Q"),
            new StringBuffer("E"), new StringBuffer("V"),
            new StringBuffer("J"), new StringBuffer("Z"),
            new StringBuffer("Z"), new StringBuffer("Z"), new StringBuffer("Z") };
    private static final Rotor ROTOR_1 = new Rotor(rotorI, notches[0]);
    private static final Rotor ROTOR_2 = new Rotor(rotorII, notches[1]);
    private static final Rotor ROTOR_3 = new Rotor(rotorIII, notches[2]);
    private static final Rotor ROTOR_4 = new Rotor(rotorIV, notches[3]);
    private static final Rotor ROTOR_5 = new Rotor(rotorV, notches[4]);
    private static final Rotor ROTOR_6 = new Rotor(rotorVI, notches[5]);
    private static final Rotor ROTOR_7 = new Rotor(rotorI, notches[6]);
    private static final Rotor ROTOR_8 = new Rotor(rotorI, notches[7]);
    private static final Rotor REF_B = new Rotor(reflectorB, new StringBuffer(
            ""));
    private static final Rotor REF_C = new Rotor(reflectorC, new StringBuffer(
            ""));
    private static final Rotor REF_0 = new Rotor(reflector0, new StringBuffer(
            ""));

    // CURRENT ROTORS AND REFLECTOR IN USE
    public Rotor firstRotor;
    public Rotor secondRotor;
    public Rotor thirdRotor;
    public int pos1;
    public int pos2;
    public int pos3;
    public int rin1;
    public int rin2;
    public int rin3;
    public Rotor reflector;

    // CURRENT PLUGBOARD SETTINGS
    public char[] plugBoard = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z' };

    /**
     * Class Constructor
     *
     * @param r1
     *            rotor to be used as first rotor
     * @param r2
     *            rotor to be used as second rotor
     * @param r3
     *            rotor to be used as third rotor
     */
    public Enigma(String r1, String r2, String r3) {
        firstRotor = getRotor(r1);
        secondRotor = getRotor(r2);
        thirdRotor = getRotor(r3);
        reflector = REF_B;
    }

    private Rotor getRotor(String v) {
        if (v.equals("RotorI") || v.equals("1")) {
            return ROTOR_1;
        }
        if (v.equals("RotorII") || v.equals("2")) {
            return ROTOR_2;
        }
        if (v.equals("RotorIII") || v.equals("3")) {
            return ROTOR_3;
        }
        if (v.equals("RotorIV") || v.equals("4")) {
            return ROTOR_4;
        }
        if (v.equals("RotorV") || v.equals("5")) {
            return ROTOR_5;
        }
        if (v.equals("RotorVI") || v.equals("6")) {
            return ROTOR_6;
        }
        if (v.equals("RotorVII") || v.equals("7")) {
            return ROTOR_7;
        }
        if (v.equals("RotorVIII") || v.equals("8")) {
            return ROTOR_8;
        }
        throw new IllegalArgumentException(v);
    }

    /**
     * Sets the first Rotor
     *
     * @param r1
     *            rotor to be used as first rotor
     * @return void
     */
    public void setFirstRotor(String r1) {
        firstRotor = getRotor(r1);
    }

    /**
     * Sets the second Rotor
     *
     * @param r2
     *            rotor to be used as second rotor
     * @return void
     */
    public void setSecondRotor(String r2) {
        secondRotor = getRotor(r2);
    }

    /**
     * Sets the second Rotor
     *
     * @param r3
     *            rotor to be used as third rotor
     * @return void
     */
    public void setThirdRotor(String r3) {
        thirdRotor = getRotor(r3);
    }

    /**
     * Sets the intial settings of the rotors.
     *
     * @param s1
     *            initial setting for first rotor
     * @param s2
     *            initial setting for second rotor
     * @param s3
     *            initial setting for third rotor
     * @return void
     */
    public void initialSettings(String s1, String s2, String s3, String ring1,
            String ring2, String ring3) {
        pos1 = IFromCh(s1.charAt(0));
        pos2 = IFromCh(s2.charAt(0));
        pos3 = IFromCh(s3.charAt(0));
        rin1 = IFromCh(ring1.charAt(0));
        rin2 = IFromCh(ring2.charAt(0));
        rin3 = IFromCh(ring3.charAt(0));
    }

    /**
     * Creates a plubboard connection between two letters
     *
     * @param x
     *            first character to be connected
     * @param y
     *            second character to be connected
     * @return void
     */
    public void setPlugBoard(char x, char y) {
        for (int i = 0; i < plugBoard.length; i++) {
            if (plugBoard[i] == x)
                plugBoard[i] = y;
            else if (plugBoard[i] == y)
                plugBoard[i] = x;
        }
    }

    /**
     * Sets the plug board settings
     *
     * @param str
     *            plug board settings formatted in pairs, each pair seperated by
     *            a space
     * @return boolean if str entered was in correct format and if the plugboard
     *         was set accordingly
     */
    public boolean setPlugBoard(String str) {
        String s;
        StringTokenizer tokenCheck = new StringTokenizer(str, " ");
        while (tokenCheck.hasMoreTokens()) {
            s = tokenCheck.nextToken();
            if (s.length() != 2)
                return false;
            if (s.charAt(0) > 90 || s.charAt(0) < 65 || s.charAt(1) > 90
                    || s.charAt(1) < 65)
                return false;
        }

        StringTokenizer token = new StringTokenizer(str, " ");
        while (token.hasMoreTokens()) {
            s = token.nextToken();
            if (s.length() == 2)
                setPlugBoard(s.charAt(0), s.charAt(1));
            else
                return false;
        }
        return true;
    }

    /**
     * Returns the value of the specified Rotor.
     *
     * @param v
     *            name or number of rotor
     * @return StringBuffer[] correct rotor
     */
    public StringBuffer[] getValue(String v) {
        StringBuffer[] result = new StringBuffer[] { new StringBuffer("ERROR"),
                new StringBuffer("") };
        if (v.equals("ReflectorB")) {
            result[0] = reflectorB;
            result[1] = new StringBuffer("");
        }
        if (v.equals("ReflectorC")) {
            result[0] = reflectorC;
            result[1] = new StringBuffer("");
        }
        if (v.equals("No Reflector")) {
            result[0] = reflector0;
            result[1] = new StringBuffer("");
        } else {
            throw new IllegalArgumentException(v);
        }
        result[0] = new StringBuffer(result[0]);
        result[1] = new StringBuffer(result[1]);
        return result;
    }

    /**
     * Returns the character obtained after passing l through the current first
     * rotor
     *
     * @param l
     *            character input
     * @return char obtained after passing l through the current first rotor
     */
    public int rotorOne(int i) {
        int d = firstRotor.map[(26 + i + pos1 - this.rin1) % 26];
        return (i + d) % 26;
    }

    /**
     * Returns the character obtained after passing l through the current second
     * rotor
     *
     * @param l
     *            character input
     * @return char obtained after passing l through the current second rotor
     */
    public int rotorTwo(int i) {
        int d = secondRotor.map[(26 + i + pos2 - this.rin2) % 26];
        return (i + d) % 26;
    }

    /**
     * Returns the character obtained after passing l through the current third
     * rotor
     *
     * @param l
     *            character input
     * @return char obtained after passing l through the current third rotor
     */
    public int rotorThree(int i) {
        int d = thirdRotor.map[(26 + i + pos3 - this.rin3) % 26];
        return (i + d) % 26;
    }

    /**
     * Returns the character obtained after passing l through the current
     * reflector
     *
     * @param l
     *            character input
     * @return char obtained after passing l through the current reflector
     */
    public int reflector(int i) {
        return (i + this.reflector.map[i]) % 26;
    }

    /**
     * Returns the character obtained after passing l through the current first
     * rotor in the reverse direction
     *
     * @param l
     *            character input
     * @return char obtained after passing l through the current first rotor in
     *         the reverse direction
     */
    public int IrotorOne(int i) {
        int d = firstRotor.mapRev[(26 + i + pos1 - this.rin1) % 26];
        return (i + d) % 26;
    }

    /**
     * Returns the character obtained after passing l through the current second
     * rotor in the reverse direction
     *
     * @param l
     *            character input
     * @return char obtained after passing l through the current second rotor in
     *         the reverse direction
     */
    public int IrotorTwo(int i) {
        int d = secondRotor.mapRev[(26 + i + pos2 - this.rin2) % 26];
        return (i + d) % 26;
    }

    /**
     * Returns the character obtained after passing l through the current third
     * rotor in the reverse direction
     *
     * @param l
     *            character input
     * @return char obtained after passing l through the current third rotor in
     *         the reverse direction
     */
    public int IrotorThree(int i) {
        int d = thirdRotor.mapRev[(26 + i + pos3 - this.rin3) % 26];
        return (i + d) % 26;
    }

    /**
     * Rotates the rotors according to their current settings
     *
     * @param void
     * @return void
     */
    public void rotate() {
        // Middle notch - all rotors rotate
        if (pos2 == secondRotor.notch) {
            pos1 += 1;
            pos2 += 1;
        }
        // Right notch - right two rotors rotate
        else if (pos3 == thirdRotor.notch) {
            this.pos2 += 1;
        }

        pos3++;
        this.pos1 %= 26;
        this.pos2 %= 26;
        this.pos3 %= 26;
    }

    /**
     * Returns the result of passing c through the plugboard with its current
     * settings
     *
     * @param c
     *            the inputted character
     * @return char the result of passing c through the plugboard with its
     *         current settings
     * @deprecated not fixed for JS
     */
    @Deprecated
    public char plugBoard(char c) {
        int i = (int) (c) - 65;
        return plugBoard[i];
    }

    /**
     * Returns the current setting of the first rotor.
     *
     * @param void
     * @return char that is the current setting of the first rotor
     */
    public int getFRSetting() {
        return pos1;
    }

    /**
     * Returns the current setting of the second rotor.
     *
     * @param void
     * @return char that is the current setting of the second rotor
     */
    public int getSRSetting() {
        return pos2;
    }

    /**
     * Returns the current setting of the third rotor.
     *
     * @param void
     * @return char that is the current setting of the third rotor
     */
    public int getTRSetting() {
        return pos3;
    }

    /**
     * Encrypts/Decrypts the inputted string using the machine's current
     * settings
     *
     * @param p
     *            the text to be encrypted/decrypted
     * @return void
     */
    public String encrypt(String p) {
        p = p.toUpperCase();
        String e = "";
        int c;

        // for the length of the inputted text
        for (int i = 0; i < p.length(); i++) {
            // store the current character
            c = p.charAt(i);

            // if the current character is a letter
            if (c <= 90 && c >= 65) {
                c = IFromCh(c);
                // rotate the rotors
                rotate();
                // pass the character through the plugboard
                // c = plugBoard(c);
                // then through the first rotor
                c = rotorThree(c);
                // then through the second rotor
                c = rotorTwo(c);
                // then through the third rotor
                c = rotorOne(c);
                // then through the reflector
                c = reflector(c);
                // then through the first rotor in the reverse direction
                c = IrotorOne(c);
                // then through the second rotor in the reverse direction
                c = IrotorTwo(c);
                // then through the third rotor in the reverse direction
                c = IrotorThree(c);
                // and finally back through the plugboard
                // c = plugBoard(c);
                // and add the new character to the encrypted/decrypted message
                c = ChFromI(c);
                e = e + (char)c;
            }

            // if c is a space simply add it to the encrypted/decrypted message
            // to conserve spaces
            else if (c == 32)
                e = e + c;

            // if c is neither a space nor a letter, then there is an error
            else
                return null;
        }

        // return the complete encrypted/decrpyted message
        return e;
    }

    /**
     * Parses Plugboard input to check for repeated letters as each letter can
     * only be used once in the plugboard
     *
     * @param str
     *            the inputted plug board settings
     * @return void
     */
    public boolean pbParser(String str) {
        // if no plug board settings were input, then continue
        if (str.length() <= 0 || str.equals(null) || str == null) {
            return true;
        }

        // otherwise, check to make sure letters are not repeated
        for (int i = 0; i < str.length() - 1; i++) {
            // if not a letter, continue
            if (str.charAt(i) > 90 || str.charAt(i) < 65)
                i++;
            // if the current letter appears in the rest of the string
            else if (str.substring(i + 1).indexOf(str.charAt(i)) != -1)
                return false;
        }

        // otherwise, return true
        return true;
    }

}
