// TEAM 8: BARAN ONALAN, LUOSHAN ZHANG, JOEN HO, JULIE MAMMEN
// CPSC 5210
// We labeled unit tests with whoever authored them.
//
// This is the actual implementation of IRandomizer that the Mastermind game uses.
// It generates random color codes every time it is run.

import org.junit.jupiter.api.Test;
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
}