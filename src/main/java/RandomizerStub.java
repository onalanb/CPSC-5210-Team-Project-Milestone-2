// TEAM 8: BARAN ONALAN, LUOSHAN ZHANG, JOEN HO, JULIE MAMMEN
// CPSC 5210
// We labeled any changes we had to do for the purpose of unit testing.
// We also included names next to such changes to indicate who changed them.
//
// BARAN ONALAN
// This is the mock/stub implementation of IRandomizer that the Mastermind
// unit tests use. It returns whatever fixed color codes our tests need it to return.

public class RandomizerStub implements IRandomizer {

    private String colorCode;

    public RandomizerStub(String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public String generateColorCode() {
        return colorCode;
    }

}
