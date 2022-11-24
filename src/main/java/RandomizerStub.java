// TEAM 8: BARAN ONALAN, LUOSHAN ZHANG, JOEN HO, JULIE MAMMEN
// CPSC 5210
// We labeled any changes we had to do for the purpose of unit testing.
// We also included names next to such changes to indicate who changed them.
//
// BARAN ONALAN
// This is the mock/stub implementation of IRandomizer that the Mastermind
// unit tests use. It returns whatever fixed color codes our tests need it to return.

import java.util.Queue;

public class RandomizerStub implements IRandomizer {

    private String colorCode;
    private Queue<Integer> solutionIDs;

    // This constructor gets used when testing multiple rounds and UI automation.
    public RandomizerStub(String colorCode, Queue<Integer> solutionIDs) {
        this.colorCode = colorCode;
        this.solutionIDs = solutionIDs;
    }

    // This constructor gets used when testing the human turn.
    public RandomizerStub(String colorCode) {
        this.colorCode = colorCode;
    }

    // This constructor gets used when testing the computer turn.
    public RandomizerStub(Queue<Integer> solutionIDs) {
        this.solutionIDs = solutionIDs;
    }

    @Override
    public String generateColorCode() {
        return colorCode;
    }

    @Override
    public int generateSolutionID() {
        return solutionIDs.remove();
    }
}
