// TEAM 8: BARAN ONALAN, LUOSHAN ZHANG, JOEN HO, JULIE MAMMEN
// CPSC 5210
// We labeled any changes we had to do for the purpose of unit testing.
// We also included names next to such changes to indicate who changed them.
//
// BARAN ONALAN
// This is the actual implementation of IRandomizer that the Mastermind game uses.
// It generates random color codes every time it is run.

import java.util.Random;

public class MastermindRandomizer implements IRandomizer {

    final Random random = new Random();
    final Color[] colors = Color.values();;
    final int numOfColors, positions, possibilities;

    public MastermindRandomizer(int numOfColors, int positions) {
        this.numOfColors = numOfColors;
        this.positions = positions;
        this.possibilities = (int) Math.round(Math.pow(numOfColors, positions));
    }

    /**
     * Generates a set of color codes randomly.
     */
    @Override
    public String generateColorCode() {
        int solution = generateSolutionID();
        return solutionIdToColorCode(solution);
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
    private String solutionIdToColorCode(final int solution) {
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

}
