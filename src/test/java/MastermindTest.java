// TEAM 8: BARAN ONALAN, LUOSHAN ZHANG, JOEN HO, JULIE MAMMEN
// CPSC 5210
// We labeled unit tests with whoever authored them.
//
// This is the actual implementation of IRandomizer that the Mastermind game uses.
// It generates random color codes every time it is run.

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MastermindTest {

    // BARAN ONALAN
    // This test the humanTurn() unit with the scenario of a correct guess within
    // max retries.
    @Test
    void humanTurnTestGuessRight() {
        // We provide user input for one incorrect and one correct guesses.
        LinkedList<String> guesses = new LinkedList<>();
        guesses.add("BBBB");
        guesses.add("GRBW");
        Mastermind.setInputs(guesses);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // The correct secret code is this.
        String secretCode = "GRBW";
        IRandomizer randomizer = new RandomizerStub(secretCode);
        // Max retries is 10.
        Mastermind game = new Mastermind(4, 4, 1, 10, randomizer);

        // Call the method that we are testing.
        game.humanTurn();

        // The expected output should contain lines for both incorrect and correct guesses.
        String expected = "TOTAL POSSIBILITIES = 256\r\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\r\n" +
                "BLACK        B\r\n" +
                "WHITE        W\r\n" +
                "RED          R\r\n" +
                "GREEN        G\r\n" +
                "\r\n" +
                "\r\n" +
                "GUESS MY COMBINATION. \n" +
                "\r\n" +
                "MOVE #1 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\r\n" +
                "MOVE #2 GUESS ?YOU GUESSED IT IN 2 MOVES!\r\n" +
                "SCORE:\r\n" +
                "\tCOMPUTER \t0\r\n" +
                "\tHUMAN \t2\r\n" +
                "\r\n";
        // Assert that the actual output is the same as expected.
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    // BARAN ONALAN
    // This test the humanTurn() unit with the scenario of all incorrect guesses within
    // max retries.
    @Test
    void humanTurnTestGuessWrong() {
        // We provide user input for two incorrect guesses.
        LinkedList<String> guesses = new LinkedList<>();
        guesses.add("BBBB");
        guesses.add("GGGG");
        Mastermind.setInputs(guesses);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // The correct secret code is this.
        String secretCode = "GRBW";
        IRandomizer randomizer = new RandomizerStub(secretCode);
        // Max retries is 2.
        Mastermind game = new Mastermind(4, 4, 1, 2, randomizer);

        // Call the method that we are testing.
        game.humanTurn();

        // The expected output should contain lines for all incorrect guesses
        // and that we ran out of moves.
        String expected = "TOTAL POSSIBILITIES = 256\r\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\r\n" +
                "BLACK        B\r\n" +
                "WHITE        W\r\n" +
                "RED          R\r\n" +
                "GREEN        G\r\n" +
                "\r\n" +
                "\r\n" +
                "GUESS MY COMBINATION. \n" +
                "\r\n" +
                "MOVE #1 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\r\n" +
                "MOVE #2 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\r\n" +
                "YOU RAN OUT OF MOVES!  THAT'S ALL YOU GET!\r\n" +
                "THE ACTUAL COMBINATION WAS: GRBW\r\n";
        // Assert that the actual output is the same as expected.
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    // JOEN HO
    // Parameterized tests for evaluateGuess function
    // numBlack = number of colors in the correct position
    // numWhite = number of colors present but not in the correct position
    @ParameterizedTest(name = "evaluateGuess({0}, {1}, {2}, {3}, {4}, {5})")
    @CsvSource({
            // 0 position
            "1, 0, B, B, 0, 0",
            // 1 color, 1 position
            "1, 1, B, B, 1, 0",
            "1, 1, B, W, 0, 0",
            // 1 color, 2 position
            "1, 2, BB, BB, 2, 0",
            "1, 2, BB, BW, 1, 0",
            // 2 colors, 1 positions
            "2, 1, B, B, 1, 0",
            "2, 1, B, W, 0, 0",
            "2, 1, B, BW, 1, 0",
            "2, 1, B, WB, 0, 0",
            // 2 colors, 2 positions
            "2, 2, BW, BW, 2, 0",
            "2, 2, BW, BB, 1, 0",
            "2, 2, BW, WB, 0, 2",
            // 2 colors, 3 positions
            "2, 3, BWB, BWB, 3, 0",
            "2, 3, BBB, BBW, 2, 0",
            "2, 3, BWB, WBW, 0, 2",
            // 3 colors, 3 positions
            "3, 3, BWR, BWR, 3, 0",
            "3, 3, BWR, RWB, 1, 2",
            "3, 3, BWR, RBW, 0, 3",
            "3, 3, BWR, GGG, 0, 0",
            // 4 colors, 4 positions
            "4, 4, BWRG, BWRG, 4, 0",
            "4, 4, BWRG, BRGW, 1, 3",
            "4, 4, BWRG, GRGR, 0, 2",
            "4, 4, BWRG, OOOO, 0, 0",
            // 5 colors, 5 positions
            "5, 5, BWRGO, BWRGO, 5, 0",
            "5, 5, BWRGO, BBBBB, 1, 0",
            "5, 5, BWRGO, OGYYY, 0, 2",
            "5, 5, BWRGO, YYYYY, 0, 0",
            // 6 colors, 6 positions
            "6, 6, BWRGOY, BWRGOY, 6, 0",
            "6, 6, BWRGOY, YYYYYY, 1, 0",
            "6, 6, BWRGOY, WBPPPP, 0, 2",
            "6, 6, BWRGOY, PPPPPP, 0, 0",
            // 7 colors, 7 positions
            "7, 7, BWRGOYP, BWRGOYP, 7, 0",
            "7, 7, BWRGOYP, RRRRRRR, 1, 0",
            "7, 7, BWRGOYP, WBGROYP, 3, 4",
            "7, 7, BWRGOYP, TTTTTTT, 0, 0",
            // 8 colors, 8 positions
            "8, 8, BWRGOYPT, BWRGOYPT, 8, 0",
            "8, 8, BWRGOYPT, RRRRRRRR, 1, 0",
            "8, 8, BWRGOYPT, TPYOGRWB, 0, 8",
            "8, 8, BWRGOYPT, SSSSSSSS, 0, 0",
            "9, 8, BWRGOYPT, SSSSSSSS, 0, 0",
    })
    void param_evaluateGuess(int numOfColors, int positions, String colorCode, String guess, int numBlack, int numWhite) {
        // Declare randomizer stub for fixed param testing
        RandomizerStub randStub = new RandomizerStub(colorCode);
        // Create game instance
        Mastermind game = new Mastermind(numOfColors, positions, 1, 1, randStub);
        // evaluate the test inputs
        Mastermind.Guess actual = game.evaluateGuess(0, guess, colorCode);
        assertEquals(numBlack, actual.blacks());
        assertEquals(numWhite, actual.whites());
        // print the expected and actual output
        System.out.println(String.format("### TEST param_evaluateGuess(%d, %d, %s, %s, %d, %d) ###", numOfColors, positions, colorCode, guess, numBlack, numWhite));
        System.out.println(String.format("[EXPECTED] Black=%d White=%d\n[ACTUAL] Black=%d White=%d\n", numBlack, numWhite, actual.blacks(), actual.whites()));
    }

}

