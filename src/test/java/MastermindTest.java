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

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class MastermindTest {

    // BARAN ONALAN
    // Before each test set input, so it is not null. Otherwise, SUT can execute
    // system.exit which will exit the UT as well.
    @BeforeEach
    void setUp() {
        Mastermind.setInputs(new LinkedList<>());
    }

    // BARAN ONALAN
    // This tests both play() method and playRound() method since play() calls playRound()
    // a specified number of times.
    @Test
    void playTest() {
        // BARAN ONALAN
        // THIS IS ONLY FOR THE LIVE DEMO IN CLASS!!!
        // This will always be FALSE unless we demo for class, then it will be TRUE.
        Mastermind.demoMode = false;

        LinkedList<String> userInputs = new LinkedList<>();
        // Human Turn - Round 1
        // We provide user input for two incorrect guesses.
        userInputs.add(" BB\n");
        userInputs.add(" WB\n");
        userInputs.add(" BW\n");
        // Computer Turn - Round 1
        // We provide user input for two incorrect and one correct guesses from the computer.
        // Human secret code here is "WB"
        userInputs.add("\n");
        userInputs.add("1 0\n");
        userInputs.add("0 2\n");
        userInputs.add("2 0\n");
        // Human Turn - Round 2
        // We provide user input for one incorrect and one correct guesses.
        userInputs.add(" BB\n");
        userInputs.add(" WW\n");
        // Computer Turn - Round 2
        // We provide user input for two incorrect and one invalid and incorrect guess from the computer.
        // Invalid Result
        userInputs.add("\n");
        userInputs.add("1 0\n");
        userInputs.add("0 2\n");
        userInputs.add("0 2\n");
        // Valid Result
        userInputs.add("\n");
        userInputs.add("1 0\n");
        userInputs.add("0 2\n");
        userInputs.add("2 0\n");

        Mastermind.setInputs(userInputs);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        LinkedList<Integer> computerInputs = new LinkedList<Integer>();
        // Computer Turn - Round 1
        // Computer tries three times before guessing correctly.
        computerInputs.add(3);  // WW
        computerInputs.add(1);  // BW
        computerInputs.add(2);  // WB
        // Computer Turn - Round 2
        // Computer tries three times before human gives invalid input.
        // Then computer tries three times before guessing correctly.
        // Invalid human input
        computerInputs.add(3);  // WW
        computerInputs.add(1);  // BW
        computerInputs.add(2);  // WB
        // Valid human input
        computerInputs.add(3);  // WW
        computerInputs.add(1);  // BW
        computerInputs.add(2);  // WB

        // The correct secret code is this.
        String secretCode = "WW";
        IRandomizer randomizer = new RandomizerStub(secretCode, computerInputs);
        // Max retries is 2.
        Mastermind game = new Mastermind(2, 2, 2, 3, randomizer);

        // Call the method that we are testing.
        game.play();

        // The expected output should contain lines for all incorrect guesses
        // and that we ran out of moves.
        String expected = "TOTAL POSSIBILITIES = 4\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n" +
                "\n" +
                "\n" +
                "ROUND NUMBER  1 ----\n" +
                "\n" +
                "GUESS MY COMBINATION. \n" +
                "\n" +
                "MOVE #1 GUESS ?YOU HAVE 0 BLACKS AND 0 WHITES.\n" +
                "MOVE #2 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\n" +
                "MOVE #3 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\n" +
                "YOU RAN OUT OF MOVES!  THAT'S ALL YOU GET!\n" +
                "THE ACTUAL COMBINATION WAS: WW\n" +
                "NOW I GUESS.  THINK OF A COMBINATION.\n" +
                "HIT RETURN WHEN READY:\n" +
                "MY GUESS IS: WW  BLACKS, WHITES ? " +
                "MY GUESS IS: BW  BLACKS, WHITES ? " +
                "MY GUESS IS: WB  BLACKS, WHITES ? " +
                "I GOT IT IN  3 MOVES!\n" +
                "SCORE:\n" +
                "\tCOMPUTER \t3\n" +
                "\tHUMAN \t3\n" +
                "\n" +
                "ROUND NUMBER  2 ----\n" +
                "\n" +
                "GUESS MY COMBINATION. \n" +
                "\n" +
                "MOVE #1 GUESS ?YOU HAVE 0 BLACKS AND 0 WHITES.\n" +
                "MOVE #2 GUESS ?YOU GUESSED IT IN 2 MOVES!\n" +
                "SCORE:\n" +
                "\tCOMPUTER \t3\n" +
                "\tHUMAN \t5\n" +
                "\n" +
                "NOW I GUESS.  THINK OF A COMBINATION.\n" +
                "HIT RETURN WHEN READY:\n" +
                "MY GUESS IS: WW  BLACKS, WHITES ? " +
                "MY GUESS IS: BW  BLACKS, WHITES ? " +
                "MY GUESS IS: WB  BLACKS, WHITES ? " +
                "YOU HAVE GIVEN ME INCONSISTENT INFORMATION.\n" +
                "TRY AGAIN, AND THIS TIME PLEASE BE MORE CAREFUL.\n" +
                "NOW I GUESS.  THINK OF A COMBINATION.\n" +
                "HIT RETURN WHEN READY:\n" +
                "MY GUESS IS: WW  BLACKS, WHITES ? " +
                "MY GUESS IS: BW  BLACKS, WHITES ? " +
                "MY GUESS IS: WB  BLACKS, WHITES ? " +
                "I GOT IT IN  3 MOVES!\n" +
                "SCORE:\n" +
                "\tCOMPUTER \t6\n" +
                "\tHUMAN \t5\n" +
                "\n" +
                "GAME OVER\n" +
                "FINAL SCORE: \n" +
                "SCORE:\n" +
                "\tCOMPUTER \t6\n" +
                "\tHUMAN \t5\n" +
                "\n";
        // Assert that the actual output is the same as expected.
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        assertEquals(expected, actual);
    }
    
    // Julie Mammen
    // This test only playRound() to ensure proper functionality
    // of the singular function. It simulates one round
    // of the game to ensure that game round plays
    // as should.
    @Test
    void playRoundTest()
    {
        Mastermind.demoMode = false;

        LinkedList<String> userInputs = new LinkedList<>();
        // Human Turn - Round 1
        // We provide input for two incorrect guesses.
        userInputs.add(" BB\n");
        userInputs.add(" WB\n");
        userInputs.add(" BW\n");
        // Computer Turn - Round 1
        // We provide input for two incorrect, one correct guesses from the computer.
        // Human secret code here is "WB"
        userInputs.add("\n");
        userInputs.add("1 0\n");
        userInputs.add("0 2\n");
        userInputs.add("2 0\n");

        Mastermind.setInputs(userInputs);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        LinkedList<Integer> computerInputs = new LinkedList<Integer>();
        // Computer Turn - Round 1
        // Computer tries three times before guessing correctly.
        computerInputs.add(3);  // WW
        computerInputs.add(1);  // BW
        computerInputs.add(2);  // WB

        // The correct secret code is this.
        String secretCode = "WW";
        IRandomizer randomizer = new RandomizerStub(secretCode, computerInputs);
        // Max retries is 3.
        Mastermind game = new Mastermind(2, 2, 1, 3, randomizer);

        // Call the method that we are testing, playRound().
        game.playRound(1);

        // What the round should look like to the user. 
        String expected = "TOTAL POSSIBILITIES = 4\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n" +
                "\n" +
                "\n" +
                "ROUND NUMBER  1 ----\n" +
                "\n" +
                "GUESS MY COMBINATION. \n" +
                "\n" +
                "MOVE #1 GUESS ?YOU HAVE 0 BLACKS AND 0 WHITES.\n" +
                "MOVE #2 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\n" +
                "MOVE #3 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\n" +
                "YOU RAN OUT OF MOVES!  THAT'S ALL YOU GET!\n" +
                "THE ACTUAL COMBINATION WAS: WW\n" +
                "NOW I GUESS.  THINK OF A COMBINATION.\n" +
                "HIT RETURN WHEN READY:\n" +
                "MY GUESS IS: WW  BLACKS, WHITES ? " +
                "MY GUESS IS: BW  BLACKS, WHITES ? " +
                "MY GUESS IS: WB  BLACKS, WHITES ? " +
                "I GOT IT IN  3 MOVES!\n" +
                "SCORE:\n" +
                "\tCOMPUTER \t3\n" +
                "\tHUMAN \t3\n" +
                "\n";

        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        // Assert the actual output matches the expected output.
        assertEquals(expected, actual);
    }

    // BARAN ONALAN
    // This test the humanTurn() unit with the scenario of a correct guess within
    // max retries.
    @Test
    void humanTurnTestGuessRight() {
        // BARAN ONALAN
        // THIS IS ONLY FOR THE LIVE DEMO IN CLASS!!!
        // This will always be FALSE unless we demo for class, then it will be TRUE.
        Mastermind.demoMode = false;

        // We provide user input for one incorrect and one correct guesses.
        LinkedList<String> guesses = new LinkedList<>();
        guesses.add(" BBBB\n");
        guesses.add(" GRBW\n");
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
        String expected = "TOTAL POSSIBILITIES = 256\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n" +
                "RED          R\n" +
                "GREEN        G\n" +
                "\n" +
                "\n" +
                "GUESS MY COMBINATION. \n" +
                "\n" +
                "MOVE #1 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\n" +
                "MOVE #2 GUESS ?YOU GUESSED IT IN 2 MOVES!\n" +
                "SCORE:\n" +
                "\tCOMPUTER \t0\n" +
                "\tHUMAN \t2\n" +
                "\n";
        // Assert that the actual output is the same as expected.
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        assertEquals(expected, actual);
    }

    // BARAN ONALAN
    // This test the humanTurn() unit with the scenario of all incorrect guesses within
    // max retries.
    @Test
    void humanTurnTestGuessWrong() {
        // BARAN ONALAN
        // THIS IS ONLY FOR THE LIVE DEMO IN CLASS!!!
        // This will always be FALSE unless we demo for class, then it will be TRUE.
        Mastermind.demoMode = false;

        // We provide user input for two incorrect guesses.
        LinkedList<String> guesses = new LinkedList<>();
        guesses.add(" BBBB\n");
        guesses.add(" GGGG\n");
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
        String expected = "TOTAL POSSIBILITIES = 256\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n" +
                "RED          R\n" +
                "GREEN        G\n" +
                "\n" +
                "\n" +
                "GUESS MY COMBINATION. \n" +
                "\n" +
                "MOVE #1 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\n" +
                "MOVE #2 GUESS ?YOU HAVE 1 BLACKS AND 0 WHITES.\n" +
                "YOU RAN OUT OF MOVES!  THAT'S ALL YOU GET!\n" +
                "THE ACTUAL COMBINATION WAS: GRBW\n";
        // Assert that the actual output is the same as expected.
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        assertEquals(expected, actual);
    }

    // BARAN ONALAN
    // This test the computerTurn() unit with the scenario of all human input being valid.
    @Test
    void computerTurnTestValidHumanInput() {
        // BARAN ONALAN
        // THIS IS ONLY FOR THE LIVE DEMO IN CLASS!!!
        // This will always be FALSE unless we demo for class, then it will be TRUE.
        Mastermind.demoMode = false;

        // We provide user input for two incorrect and one correct guesses from the computer.
        // Human secret code here is "WB"
        LinkedList<String> userInputs = new LinkedList<>();
        userInputs.add("\n");
        userInputs.add("1 0\n");
        userInputs.add("0 2\n");
        userInputs.add("2 0\n");
        Mastermind.setInputs(userInputs);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // Computer tries three times before guessing correctly.
        LinkedList<Integer> computerInputs = new LinkedList<Integer>();
        computerInputs.add(3);  // WW
        computerInputs.add(1);  // BW
        computerInputs.add(2);  // WB
        IRandomizer randomizer = new RandomizerStub(computerInputs);
        // Max retries is 10.
        Mastermind game = new Mastermind(2, 2, 1, 3, randomizer);

        // Call the method that we are testing.
        game.computerTurn();

        String expected = "TOTAL POSSIBILITIES = 4\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n" +
                "\n" +
                "\n" +
                "NOW I GUESS.  THINK OF A COMBINATION.\n" +
                "HIT RETURN WHEN READY:\n" +
                "MY GUESS IS: WW  BLACKS, WHITES ? " +
                "MY GUESS IS: BW  BLACKS, WHITES ? " +
                "MY GUESS IS: WB  BLACKS, WHITES ? " +
                "I GOT IT IN  3 MOVES!\n";

        // Assert that the actual output is the same as expected.
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        assertEquals(expected, actual);
    }

    // BARAN ONALAN
    // This test the computerTurn() unit with the scenario of all human input being invalid.
    @Test
    void computerTurnTestInvalidHumanInput() {
        // BARAN ONALAN
        // THIS IS ONLY FOR THE LIVE DEMO IN CLASS!!!
        // This will always be FALSE unless we demo for class, then it will be TRUE.
        Mastermind.demoMode = false;

        // We provide user input for two incorrect and one invalid and incorrect guess from the computer.
        // Human secret code here is "WB"
        LinkedList<String> userInputs = new LinkedList<>();
        // Invalid
        userInputs.add("\n");
        userInputs.add("1 0\n");
        userInputs.add("0 2\n");
        userInputs.add("0 2\n");
        // Valid
        userInputs.add("\n");
        userInputs.add("1 0\n");
        userInputs.add("0 2\n");
        userInputs.add("2 0\n");
        Mastermind.setInputs(userInputs);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // Computer tries three times before human gives invalid input.
        // Then computer tries three times before guessing correctly.
        LinkedList<Integer> computerInputs = new LinkedList<Integer>();
        // Invalid
        computerInputs.add(3);  // WW
        computerInputs.add(1);  // BW
        computerInputs.add(2);  // WB
        // Valid
        computerInputs.add(3);  // WW
        computerInputs.add(1);  // BW
        computerInputs.add(2);  // WB
        IRandomizer randomizer = new RandomizerStub(computerInputs);
        // Max retries is 10.
        Mastermind game = new Mastermind(2, 2, 1, 3, randomizer);

        // Call the method that we are testing.
        game.computerTurn();

        String expected = "TOTAL POSSIBILITIES = 4\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n" +
                "\n" +
                "\n" +
                "NOW I GUESS.  THINK OF A COMBINATION.\n" +
                "HIT RETURN WHEN READY:\n" +
                "MY GUESS IS: WW  BLACKS, WHITES ? " +
                "MY GUESS IS: BW  BLACKS, WHITES ? " +
                "MY GUESS IS: WB  BLACKS, WHITES ? " +
                "YOU HAVE GIVEN ME INCONSISTENT INFORMATION.\n" +
                "TRY AGAIN, AND THIS TIME PLEASE BE MORE CAREFUL.\n" +
                "NOW I GUESS.  THINK OF A COMBINATION.\n" +
                "HIT RETURN WHEN READY:\n" +
                "MY GUESS IS: WW  BLACKS, WHITES ? " +
                "MY GUESS IS: BW  BLACKS, WHITES ? " +
                "MY GUESS IS: WB  BLACKS, WHITES ? " +
                "I GOT IT IN  3 MOVES!\n";

        // Assert that the actual output is the same as expected.
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
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
    }

    // Julie Mammen
    // This tests generateSolutionID with parameterized testing.
    // It utilizes the RandomizerStub in order to populate
    // a linked list to ensure that solutionID is generated and correct.
    @ParameterizedTest(name = "generateCorrectSolutionID: solutionId={0} => ")
    @CsvSource({
            "0",
            "1",
            "2",
            "3",
    })
    void generateCorrectSolutionID(int expected)
    {
        LinkedList<Integer> ID = new LinkedList<>();
        ID.add(expected);
        // Create Mastermind with RandomizerStub to control the SolutionID generation
        IRandomizer randomizer = new RandomizerStub(ID);
        Mastermind game = new Mastermind(2, 2, 1, 2, randomizer);
        // Asserts that the actual result is the same as expected.
        assertEquals(expected, randomizer.generateSolutionID());
    }
    
    // Luoshan Zhang
    // This test solutionIdToColorCodeRight() using parameterized tests with
    // given valid id and check if the color code is decoded correctly
    @ParameterizedTest(name = "solutionIdToColorCodeRight: solutionId={0} => " +
            "colorCode= {1}")
    @CsvSource({
            "0, BB",
            "1, BW",
            "2, WB",
            "3, WW",
    })
    void solutionIdToColorCodeRight(int solutionId, String expectedColorCode) {
        String secretCode = "WB";

        // Create Mastermind with RandomizerStub to using the giving secret code
        IRandomizer randomizer = new RandomizerStub(secretCode);
        Mastermind game = new Mastermind(2, 2, 1, 2, randomizer);

        // Assert that the actual result is the same as expected.
        assertTrue(expectedColorCode.equals(game.solutionIdToColorCode(solutionId)));
    }

    // This test validateGuess() using parameterized tests
    @ParameterizedTest(name = "isGuessValid: guess={0} => isValid= {1}")
    @CsvSource({
            "BB, true",    // valid value with valid length
            "BW, true",
            "WB, true",
            "WW, true",
            "BBBB, false", // valid color, but length is greater than position
            "B, false",    // valid color, but length is less than position
            "AA, false",   // valid length, invalid color
    })
    void validateGuess(String guess, boolean expectedValidateGuess) {
        String secretCode = "WB";

        // Create Mastermind with RandomizerStub to using the giving secret code
        IRandomizer randomizer = new RandomizerStub(secretCode);
        Mastermind game = new Mastermind(2, 2, 1, 2, randomizer);

        // Assert that the actual result is the same as expected.
        assertTrue(expectedValidateGuess == game.validateGuess(guess));
    }

    // JOEN HO
    // This is parameterized tests for getMaxPositions function
    @ParameterizedTest(name = "param_getMaxPositionsTest: numOfColors={0} => expected= {1}")
    @CsvSource({
            "2147483647, 1",
            "1, 2147483647",
            "2, 30",
            "3, 19",
            "4, 15",
            "5, 13",
            "6, 11",
            "7, 11",
            "8, 10",
    })
    void param_getMaxPositionsTest(int numOfColors, int expected) {
        assertEquals(expected, Mastermind.getMaxPositions(numOfColors));
    }

    // JOEN HO
    // This is unit test for printGuess function.
    @Test
    void printGuessTest() {
        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));
        // Declare randomizer stub for fixed param testing
        RandomizerStub randStub = new RandomizerStub("BWW");
        // Create game instance
        Mastermind game = new Mastermind(2, 3, 1, 1, randStub);
        Mastermind.Guess guess = new Mastermind.Guess(1, "BBW",2, 1);
        // call printGuess
        game.printGuess(guess);
        String expected = "TOTAL POSSIBILITIES = 8\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n" +
                "\n" +
                "\n" +
                "  1      BBW              2         1\n";
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        // compare expected and actual output
        assertEquals(expected, actual);
    }

    // JOEN HO
    @Test
    void displayBoardTest() {
        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));
        // Declare randomizer stub for fixed param testing
        RandomizerStub randStub = new RandomizerStub("BWW");
        // Create game instance
        Mastermind game = new Mastermind(2, 3, 1, 1, randStub);
        // Add guesses
        game.guesses.add(new Mastermind.Guess(1, "WBW",1, 2));
        game.guesses.add(new Mastermind.Guess(1, "BBW",2, 1));
        // call printGuess
        game.displayBoard();
        // set expected
        String expected = "TOTAL POSSIBILITIES = 8\n" +
                "\n" +
                "\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n" +
                "\n" +
                "\n" +
                "\n" +
                "BOARD\n" +
                "MOVE     GUESS          BLACK     WHITE\n" +
                "  1      WBW              1         2\n" +
                "  1      BBW              2         1\n" +
                "\n";
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        // compare expected and actual output
        assertEquals(expected, actual);
    }

    // JOEN HO
    // This is parameterized tests for displayColorCodesTest function
    @ParameterizedTest(name = "param_displayColorCodesTest: numOfColors={0}")
    @CsvSource({
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
    })
    void param_displayColorCodesTest(int numOfColors) {
        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));
        // Create expected output
        String expected = "\n\nCOLOR     LETTER\n=====     ======\n";
        String colors = "";
        int count = 0;
        for (Color c : Color.values()) {
            colors += c.name + " ".repeat(13 - c.name.length()) + c + "\n";
            count += 1;
            if (numOfColors == count) {
                break;
            }
        }
        expected += colors + "\n\n";
        // call displayColorCodes
        Mastermind.displayColorCodes(numOfColors);
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        // compare expected and actual output
        assertEquals(expected, actual);
    }

    // JOEN HO
    // This is unit test for title function.
    @Test
    void titleTest() {
        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));
        String expected = "                              MASTERMIND\n" +
                "               CREATIVE COMPUTING  MORRISTOWN, NEW JERSEY%n%n%n\n\n";
        // call title
        Mastermind.title();
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        // compare expected and actual output
        assertEquals(expected, actual);
    }

    // JOEN HO
    // This is parameterized tests for getPegCountTest function
    @ParameterizedTest(name = "param_getPegCountTest: upperBound={0}, userInput={1}, userInput2={2}")
    @CsvSource({
            "5, 0 0,",
            "5, 1 1,",
            "5, 5 5,",
            "5, 4 4,",
            "5, 6 6, 2 2",
    })
    void param_getPegCountTest(int upperBound, String userInput, String userInput2){
        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // Set user input to input stream
        LinkedList<String> userInputs = new LinkedList<>();
        userInputs.add(userInput);
        if(userInput2 != null){
            userInputs.add(userInput2);
        }
        Mastermind.setInputs(userInputs);

        // set expected
        int[] nums = {Integer.MAX_VALUE, Integer.MAX_VALUE};
        if(userInput2 == null) {
            String[] numbers = userInput.split("[\\s,]+");
            nums[0] = Integer.parseInt(numbers[0].trim());
            nums[1] = Integer.parseInt(numbers[1].trim());
        }else{
            String[] numbers = userInput2.split("[\\s,]+");
            nums[0] = Integer.parseInt(numbers[0].trim());
            nums[1] = Integer.parseInt(numbers[1].trim());
        }

        // call getPegCount
        int[] ret = Mastermind.getPegCount(upperBound);

        if(nums[0] >= 0 && nums[0] <= upperBound || nums[1] >= 0 && nums[1] <= upperBound) {
            // compare expected and actual output
            assertEquals(nums[0], ret[0]);
            assertEquals(nums[1], ret[1]);
        } else {
            // set expected
            String expected = "NUMBERS MUST BE FROM 0 TO " + Integer.toString(upperBound) + ".";
            String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
            // compare expected and actual output
            assertEquals(expected, actual);
        }
    }

    // BARAN ONALAN
    // This is a parameterized test for the getPositiveNumber function
    @ParameterizedTest(name = "param_getPositiveNumberTest: userInput={0}")
    @CsvSource({
            "5",
            "3",
            "0",
            "-5",
            "hello",
    })
    void param_getPositiveNumberTest(String userInput) {
        // Set user input to input stream.
        LinkedList<String> userInputs = new LinkedList<>();
        userInputs.add(userInput);
        Mastermind.setInputs(userInputs);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // Call getPositiveNumber
        int actual = Mastermind.getPositiveNumber();

        // Set the expected output
        int expected = 0;
        boolean isError = false;
        try {
            expected = Integer.parseInt(userInput);
            if (expected < 1) {
                isError = true;
            }
        } catch (NumberFormatException invalidInput) {
            isError = true;
        }

        // Compare actual to expected
        if (!isError) {
            assertEquals(expected, actual);
        } else {
            String actualWhenError = outContent.toString().replaceAll("\\r\\n?", "\n");
            String expectedWhenError = "!NUMBER EXPECTED - RETRY INPUT LINE\n? ";
            assertEquals(expectedWhenError, actualWhenError);
        }
    }

    // BARAN ONALAN
    // This is a parameterized test for the getPositiveNumberUpTo function
    @ParameterizedTest(name = "param_getPositiveNumberUpToTest: upperBound = {0}, userInput={1}")
    @CsvSource({
            "10,5",
            "10,15",
            "10,10",
            "10,0",
            "10,-5",
            "10,hello",
    })
    void param_getPositiveNumberUpToTest(long upperBound, String userInput) {
        // Set user input to input stream.
        LinkedList<String> userInputs = new LinkedList<>();
        userInputs.add(userInput);
        Mastermind.setInputs(userInputs);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // Call getPositiveNumberUpTo
        int actual = Mastermind.getPositiveNumberUpTo(upperBound);

        // Set the expected output
        int expected = 0;
        boolean isError = false;
        try {
            expected = Integer.parseInt(userInput);
            if (expected < 1 || expected > upperBound) {
                isError = true;
            }
        } catch (NumberFormatException invalidInput) {
            isError = true;
        }

        // Compare actual to expected
        if (!isError) {
            assertEquals(expected, actual);
        } else {
            String actualWhenError = outContent.toString().replaceAll("\\r\\n?", "\n");
            String expectedWhenError = "!NUMBER FROM 1 TO 10 EXPECTED - RETRY INPUT LINE\n? ";
            assertEquals(expectedWhenError, actualWhenError);
        }
    }

    // BARAN ONALAN
    // Unit test to verify the quit function.
    @Test
    void quitTest() {
        Mastermind test = new Mastermind(4, 4, 1, 10);
        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));
        // Call quit
        test.quit("RGBB");

        // Compare actual to expected
        String normalizedOutContent = outContent.toString().replaceAll("\\r\\n?", "\n");
        assertEquals("QUITTER!  MY COMBINATION WAS: RGBB\nGOOD BYE\n", normalizedOutContent);
    }

    // Luoshan Zhang
    // This parameterized test is to check get user input as string
    @ParameterizedTest(name = "getWord: userInput={0} => expected= {1}")
    @CsvSource({
            "BW, BW",
            "BOARD, BOARD",
            "QUIT, QUIT",
            "123, 123",
            "%#, %#",
    })
    void getWordTest(String userInput, String expected) {
        // Set user input to input stream.
        LinkedList<String> userInputs = new LinkedList<>();
        userInputs.add(userInput);
        Mastermind.setInputs(userInputs);

        // Assert that the actual result is the same as expected.
        assertEquals(expected, Mastermind.getWord());
    }

    // Luoshan Zhang
    // This test is to check get score function
    @Test
    void getScoreTest() {
        // Create Mastermind with RandomizerStub to using the giving secret code
        String secretCode = "WB";
        IRandomizer randomizer = new RandomizerStub(secretCode);
        Mastermind game = new Mastermind(2, 2, 1, 2, randomizer);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // Simulate user input for one correct guesses.
        LinkedList<String> guesses = new LinkedList<>();
        guesses.add("WB");
        Mastermind.setInputs(guesses);

        // Human score is increased one after one correct guess
        game.humanTurn();
        String expected = "GUESS MY COMBINATION. \n\n" +
                "MOVE #1 GUESS ?YOU GUESSED IT IN 1 MOVES!\n" +
                "SCORE:\n" +
                "\tCOMPUTER \t0\n" +
                "\tHUMAN \t1\n\n";
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        // Assert that the actual result is the same as expected.
        assertEquals(expected, actual);
    }

    // Luoshan Zhang
    // This test is to check setup function
    @Test
    void setupTest() {
        // Simulate user input for setup
        LinkedList<String> inputs = new LinkedList<>();
        inputs.add("2");
        inputs.add("2");
        inputs.add("1");
        inputs.add("2");
        Mastermind.setInputs(inputs);

        // Redirect the output so we can compare it against the expected.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Mastermind.setOut(new PrintStream(outContent));

        // Call the testing function
        Mastermind game = Mastermind.setup();

        // The expected output after setup
        String expected = "NUMBER OF COLORS? > NUMBER OF POSITIONS (MAX 30)" +
                "? > NUMBER OF ROUNDS? > ON YOUR TURN YOU CAN ENTER " +
                "'BOARD' TO DISPLAY YOUR PREVIOUS GUESSES,\nOR 'QUIT' TO GIVE" +
                " UP.\nTOTAL POSSIBILITIES = 4\n\n\n" +
                "COLOR     LETTER\n" +
                "=====     ======\n" +
                "BLACK        B\n" +
                "WHITE        W\n\n\n";
        // Assert that the actual output is the same as expected.
        String actual = outContent.toString().replaceAll("\\r\\n?", "\n");
        assertEquals(expected, actual);
    }
}
