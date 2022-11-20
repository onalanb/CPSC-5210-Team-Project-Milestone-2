// TEAM 8: BARAN ONALAN, LUOSHAN ZHANG, JOEN HO, JULIE MAMMEN
// CPSC 5210
// We labeled any changes we had to do for the purpose of unit testing.
// We also included names next to such changes to indicate who changed them.
//
// The original source code for this file can be found here:
// https://github.com/coding-horror/basic-computer-games/blob/main/60_Mastermind/java/Mastermind.java

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A port of the BASIC Mastermind game in java.
 *
 * Differences between this and the original BASIC:
 *    Uses a number base conversion approach to converting solution ids to
 *    color code strings. The original performs an inefficient add by 1
 *    with carry technique where every carry increments the next positions
 *    color id.
 *
 *    Implements a ceiling check on the number of positions in a secret code
 *    to not run out of memory. Because of the algorithm that the computer
 *    uses to deduce the players secret code, it searches through the entire
 *    possible spectrum of solutions. This can be a very large number because
 *    it's (number of colors) ^ (number of positions). The original will
 *    happily try to allocate all the memory on the system if this number is
 *    too large. If it did successfully allocate the memory on a large solution
 *    set then it would also take too long to compute code strings via its
 *    technique mentioned in the previous note.
 *
 *    An extra message is given at the start to alert the player to the
 *    BOARD and QUIT commands.
 */
public class Mastermind {
    final Random random = new Random();

    // BARAN ONALAN
    // Since all the inputs are read from System.in via a scanner in the
    // static method getInput(), we need to have a way of feeding a sequence
    // of inputs that will replace the human provided inputs during unit testing.
    private static Queue<String> inputs = null;
    // BARAN ONALAN
    // Our unit tests will use this setter to set the inputs variable above.
    static void setInputs(Queue<String> testInputs) {
        inputs = testInputs;
    }
    // BARAN ONALAN
    // We can only check what the game is writing to the console. For that
    // we need to redirect the output to something that the unit tests can read.
    // Our unit tests will replace this out value to something other than System.out
    private static PrintStream out = System.out;
    // BARAN ONALAN
    // Our unit tests will use this setter to set the out variable above.
    static void setOut(PrintStream outStub) { out = outStub; }

    // BARAN ONALAN
    // We use out instead of System.out in these printing methods because that
    // is what we are mocking/stubbing using the methods and variables above.
    // some less verbose printing methods
    static private void pf(String s, Object... o) { out.printf(s, o);}
    static private void pl(String s) { out.println(s);}
    static private void pl() { out.println();}

    public static void main(String[] args) {
        title();
        Mastermind game = setup();
        game.play();
    }

    /**
     * Represents a guess and the subsequent number of colors in the correct
     * position (blacks), and the number of colors present but not in the correct
     * position (whites.)
     */
    // JOEN HO
    // Changed Guess to be package-private from private for testing purpose.
    record Guess(int guessNum, String guess, int blacks, int whites){}


    private void play() {
        IntStream.rangeClosed(1,rounds).forEach(this::playRound);
        pl("GAME OVER");
        pl("FINAL SCORE: ");
        pl(getScore());
    }

    /**
     * Builder-ish pattern for creating Mastermind game
     * @return Mastermind game object
     */
    private static Mastermind setup() {
        int numOfColors;
        pf("NUMBER OF COLORS? > ");
        numOfColors = getPositiveNumberUpTo(Color.values().length);
        int maxPositions = getMaxPositions(numOfColors);
        pf("NUMBER OF POSITIONS (MAX %d)? > ", maxPositions);
        int positions = getPositiveNumberUpTo(maxPositions);
        pf("NUMBER OF ROUNDS? > ");
        int rounds = getPositiveNumber();
        pl("ON YOUR TURN YOU CAN ENTER 'BOARD' TO DISPLAY YOUR PREVIOUS GUESSES,");
        pl("OR 'QUIT' TO GIVE UP.");
        return new Mastermind(numOfColors, positions, rounds, 10);
    }

    /**
     * Computes the number of allowable positions to prevent the total possible
     * solution set that the computer has to check to a reasonable number, and
     * to prevent out of memory errors.
     *
     * The computer guessing algorithm uses a BitSet which has a limit of 2^31
     * bits (Integer.MAX_VALUE bits). Since the number of possible solutions to
     * any mastermind game is (numColors) ^ (numPositions) we need find the
     * maximum number of positions by finding the Log|base-NumOfColors|(2^31)
     *
     * @param numOfColors  number of different colors
     * @return             max number of positions in the secret code.
     */
    // JOEN HO
    // I got rid of private modifier for testing purpose.
    static int getMaxPositions(int numOfColors){
        return (int)(Math.log(Integer.MAX_VALUE)/Math.log(numOfColors));
    }

    final int numOfColors, positions, rounds, possibilities;
    int humanMoves, computerMoves;
    final BitSet solutionSet;
    final Color[] colors;
    final int maxTries;
    // BARAN ONALAN
    // For unit testing we need to be able to mock/stub the random behavior.
    // We use the IRandomizer interface to stub such behavior.
    private IRandomizer randomizer;

    // A recording of human guesses made during the round for the BOARD command.
    final List<Guess> guesses = new ArrayList<>();

    // A regular expression to validate user guess strings
    final String guessValidatorRegex;

    // BARAN ONALAN
    // This is the constructor that the actual game setup uses. It uses
    // MastermindRandomizer that actually produces random words.
    public Mastermind(int numOfColors, int positions, int rounds, int maxTries) {
        this(numOfColors, positions, rounds, maxTries, new MastermindRandomizer(numOfColors, positions));
    }

    // BARAN ONALAN
    // This is the constructor that our unit tests use. It uses RandomizerStub that
    // returns fixed words per the needs of our unit tests.
    Mastermind(int numOfColors, int positions, int rounds, int maxTries, IRandomizer randomizer) {
        // BARAN ONALAN
        // This is where we set the randomizer.
        this.randomizer = randomizer;
        this.numOfColors = numOfColors;
        this.positions = positions;
        this.rounds = rounds;
        this.maxTries = maxTries;
        this.humanMoves = 0;
        this.computerMoves = 0;
        String colorCodes = Arrays.stream(Color.values())
                .limit(numOfColors)
                .map(Color::toString)
                .collect(Collectors.joining());
        // regex that limits the number of color codes and quantity for a guess.
        this.guessValidatorRegex = "^[" + colorCodes + "]{" + positions + "}$";
        this.colors = Color.values();
        this.possibilities = (int) Math.round(Math.pow(numOfColors, positions));
        pf("TOTAL POSSIBILITIES =% d%n", possibilities);
        this.solutionSet = new BitSet(possibilities);
        displayColorCodes(numOfColors);
    }

    private void playRound(int round) {
        pf("ROUND NUMBER % d ----%n%n",round);
        humanTurn();
        computerTurn();
        pl(getScore());
    }

    // BARAN ONALAN
    // humanTurn() was private previously and I changed it to default access to
    // be able to unit test.
    void humanTurn() {
        guesses.clear();
        String secretCode = randomizer.generateColorCode();
        pl("GUESS MY COMBINATION. \n");
        int guessNumber = 1;
        while (true) {   // User input loop
            pf("MOVE #%d GUESS ?", guessNumber);
            final String guess = getWord();
            if (guess.equals(secretCode)) {
                guesses.add(new Guess(guessNumber, guess, positions, 0));
                pf("YOU GUESSED IT IN %d MOVES!%n", guessNumber);
                humanMoves++;
                pl(getScore());
                return;
            } else if ("BOARD".equals(guess)) {  displayBoard();
            } else if ("QUIT".equals(guess))  {  quit(secretCode);
            } else if (!validateGuess(guess)) {  pl(guess + " IS UNRECOGNIZED.");
            } else {
                Guess g = evaluateGuess(guessNumber, guess, secretCode);
                pf("YOU HAVE %d BLACKS AND %d WHITES.%n", g.blacks(), g.whites());
                guesses.add(g);
                humanMoves++;
                guessNumber++;
            }
            if (guessNumber > maxTries) {
                pl("YOU RAN OUT OF MOVES!  THAT'S ALL YOU GET!");
                pl("THE ACTUAL COMBINATION WAS: " + secretCode);
                return;
            }
        }
    }

    private void computerTurn(){
        while (true) {
            pl("NOW I GUESS.  THINK OF A COMBINATION.");
            pl("HIT RETURN WHEN READY:");
            solutionSet.set(0, possibilities);  // set all bits to true
            getInput("RETURN KEY", Scanner::nextLine, Objects::nonNull);
            int guessNumber = 1;
            while(true){
                if (solutionSet.cardinality() == 0) {
                    // user has given wrong information, thus we have cancelled out
                    // any remaining possible valid solution.
                    pl("YOU HAVE GIVEN ME INCONSISTENT INFORMATION.");
                    pl("TRY AGAIN, AND THIS TIME PLEASE BE MORE CAREFUL.");
                    break;
                }
                // Randomly pick an untried solution.
                int solution = solutionSet.nextSetBit(generateSolutionID());
                if (solution == -1) {
                    solution = solutionSet.nextSetBit(0);
                }
                String guess = solutionIdToColorCode(solution);
                pf("MY GUESS IS: %s  BLACKS, WHITES ? ",guess);
                int[] bAndWPegs = getPegCount(positions);
                if (bAndWPegs[0] == positions) {
                    pf("I GOT IT IN % d MOVES!%n", guessNumber);
                    computerMoves+=guessNumber;
                    return;
                }
                // wrong guess, first remove this guess from solution set
                solutionSet.clear(solution);
                int index = 0;
                // Cycle through remaining solution set, marking any solutions as invalid
                // that don't exactly match what the user said about our guess.
                while ((index = solutionSet.nextSetBit(index)) != -1) {
                    String solutionStr = solutionIdToColorCode(index);
                    Guess possibleSolution = evaluateGuess(0, solutionStr, guess);
                    if (possibleSolution.blacks() != bAndWPegs[0] ||
                            possibleSolution.whites() != bAndWPegs[1]) {
                        solutionSet.clear(index);
                    }
                    index++;
                }
                guessNumber++;
            }
        }
    }

    // JOEN HO
    // evaluateGuess() was private previously.
    // I made it to package-private access for test purpose.
    // tally black and white pegs
    Guess evaluateGuess(int guessNum, String guess, String secretCode) {
        int blacks = 0, whites = 0;
        char[] g = guess.toCharArray();
        char[] sc = secretCode.toCharArray();
        // An incremented number that marks this position as having been counted
        // as a black or white peg already.
        char visited = 0x8000;
        // Cycle through guess letters and check for color and position match
        // with the secretCode. If both match, mark it black.
        // Else cycle through remaining secretCode letters and check if color
        // matches. If this matches, a preventative check must be made against
        // the guess letter matching the secretCode letter at this position in
        // case it would be counted as a black in one of the next passes.
        for (int j = 0; j < positions; j++) {
            if (g[j] == sc[j]) {
                blacks++;
                g[j] = visited++;
                sc[j] = visited++;
            }
            for (int k = 0; k < positions; k++) {
                if (g[j] == sc[k] && g[k] != sc[k]) {
                    whites++;
                    g[j] = visited++;
                    sc[k] = visited++;
                }
            }
        }
        return new Guess(guessNum, guess, blacks, whites);
    }

    // Luoshan Zhang
    // validateGuess() was private previously and I changed it to
    // default access to be able to unit test.
    boolean validateGuess(String guess) {
        return guess.length() == positions && guess.matches(guessValidatorRegex);
    }

    private String getScore() {
        return "SCORE:%n\tCOMPUTER \t%d%n\tHUMAN \t%d%n"
                .formatted(computerMoves, humanMoves);
    }

    // JOEN HO
    // I got rid of private modifier for testing purpose.
    void printGuess(Guess g){
        pf("% 3d%9s% 15d% 10d%n",g.guessNum(),g.guess(),g.blacks(),g.whites());
    }

    // JOEN HO
    // I got rid of private modifier for testing purpose.
    void displayBoard() {
        pl();
        pl("BOARD");
        pl("MOVE     GUESS          BLACK     WHITE");
        guesses.forEach(this::printGuess);
        pl();
    }

    void quit(String secretCode) {
        pl("QUITTER!  MY COMBINATION WAS: " + secretCode);
        pl("GOOD BYE");
        if (!inUnitTesting()) {
            System.exit(1);
        }
    }

    /**
     * From the total possible number of solutions created at construction, choose
     * one randomly.
     *
     * @return one of many possible solutions
     */
    private int generateSolutionID() {
        return random.nextInt(0, this.possibilities);
    }

    // Luoshan Zhang
    // solutionIdToColorCode() was private previously and I changed it to
    // default access to be able to unit test.
    /**
     * Given the number of colors and positions in a secret code, decode one of
     * those permutations, a solution number, into a string of letters
     * representing colored pegs.
     *
     * The pattern can be decoded easily as a number with base `numOfColors` and
     * `positions` representing the digits. For example if numOfColors is 5 and
     * positions is 3 then the pattern is converted to a number that is base 5
     * with three digits. Each digit then maps to a particular color.
     *
     * @param solution one of many possible solutions
     * @return String representing this solution's color combination.
     */
    String solutionIdToColorCode(final int solution) {
        StringBuilder secretCode = new StringBuilder();
        int pos = possibilities;
        int remainder = solution;
        for (int i = positions - 1; i > 0; i--) {
            pos = pos / numOfColors;
            secretCode.append(colors[remainder / pos].toString());
            remainder = remainder % pos;
        }
        secretCode.append(colors[remainder].toString());
        return secretCode.toString();
    }

    // JOEN HO
    // I got rid of private modifier for testing purpose.
    static void displayColorCodes(int numOfColors) {
        pl("\n\nCOLOR     LETTER\n=====     ======");
        Arrays.stream(Color.values())
                .limit(numOfColors)
                .map(c -> c.name + " ".repeat(13 - c.name.length()) + c)
                .forEach(Mastermind::pl);
        pl();pl();
    }

    // JOEN HO
    // I got rid of private modifier for testing purpose.
    static void title() {
        pl("""    
                                  MASTERMIND
                   CREATIVE COMPUTING  MORRISTOWN, NEW JERSEY%n%n%n
    """);
    }

    /////////////////////////////////////////////////////////
    // User input functions from here on

    // BARAN ONALAN
    // Returns true if user inputs are being mocked, which is going
    // to be the case while unit testing.
    private static boolean inUnitTesting() {
        return inputs != null;
    }

    /**
     * Base input function to be called from a specific input function.
     * Re-prompts upon unexpected or invalid user input.
     * Discards any remaining input in the line of user entered input once
     * it gets what it wants.
     * @param descriptor  Describes explicit type of input expected
     * @param extractor   The method performed against a Scanner object to parse
     *                    the type of input.
     * @param conditional A test that the input meets a minimum validation.
     * @param <T>         Input type returned.
     * @return            input type for this line of user input.
     */
    private static <T> T getInput(String descriptor,
                                  Function<Scanner, T> extractor,
                                  Predicate<T> conditional) {

        // BARAN ONALAN
        // Inputs will be null when the actual game is running. We use System.in
        // for the user inputs if so.
        // Inputs will be a non-null queue when the unit tests are running. We use
        // the input sequence in the queue replacing any user inputs if so.
        Scanner scanner;
        if (!inUnitTesting()) {
            scanner = new Scanner(System.in);
        } else {
            String input = inputs.remove();
            InputStream inStream = new ByteArrayInputStream(input.getBytes());
            scanner = new Scanner(inStream);
        }
        while (true) {
            try {
                T input = extractor.apply(scanner);
                if (conditional.test(input)) {
                    return input;
                }
            } catch (Exception ex) {
                try {
                    // If we are here then a call on the scanner was most likely unable to
                    // parse the input. We need to flush whatever is leftover from this
                    // line of interactive user input so that we can re-prompt for new input.
                    scanner.nextLine();
                } catch (Exception ns_ex) {
                    // if we are here then the input has been closed, or we received an
                    // EOF (end of file) signal, usually in the form of a ctrl-d or
                    // in the case of Windows, a ctrl-z.
                    if (!inUnitTesting()) {
                        // Exit only if we are not running the unit tests.
                        pl("END OF INPUT, STOPPING PROGRAM.");
                        System.exit(1);
                    } else {
                        // We will enter here when testing with invalid values.
                        return null;
                    }
                }
            }
            pf("!%s EXPECTED - RETRY INPUT LINE%n? ", descriptor);
        }
    }

    static int getPositiveNumber() {
        Integer input = getInput("NUMBER", Scanner::nextInt, num -> num > 0);
        return input == null ? 0 : input.intValue();
    }

    static int getPositiveNumberUpTo(long to) {
        Integer input = getInput(
                "NUMBER FROM 1 TO " + to,
                Scanner::nextInt,
                num -> num > 0 && num <= to);
        return input == null ? 0 : input.intValue();
    }

    // JOEN HO
    // I got rid of private modifier for testing purpose.
    static int[] getPegCount(int upperBound) {
        int[] nums = {Integer.MAX_VALUE, Integer.MAX_VALUE};
        while (true) {
            String input = getInput(
                    "NUMBER, NUMBER",
                    Scanner::nextLine,
                    s -> s.matches("\\d+[\\s,]+\\d+$"));
            String[] numbers = input.split("[\\s,]+");
            nums[0] = Integer.parseInt(numbers[0].trim());
            nums[1] = Integer.parseInt(numbers[1].trim());
            if (nums[0] <= upperBound && nums[1] <= upperBound &&
                    nums[0] >= 0 && nums[1] >= 0) {
                return nums;
            }
            pf("NUMBERS MUST BE FROM 0 TO %d.%n? ", upperBound);
        }
    }

    private static String getWord() {
        return getInput("WORD", Scanner::next, word -> !"".equals(word));
    }
}
