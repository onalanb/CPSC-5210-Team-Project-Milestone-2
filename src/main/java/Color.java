// TEAM 8: BARAN ONALAN, LUOSHAN ZHANG, JOEN HO, JULIE MAMMEN
// CPSC 5210
// We labeled any changes we had to do for the purpose of unit testing.
// We also included names next to such changes to indicate who changed them.
//
// BARAN ONALAN
// Refactored this out of Mastermind for MastermindRandomizer.

public enum Color {
    B("BLACK"), W("WHITE"), R("RED"), G("GREEN"),
    O("ORANGE"), Y("YELLOW"), P("PURPLE"), T("TAN");
    public final String name;

    Color(String name) {
        this.name = name;
    }
}