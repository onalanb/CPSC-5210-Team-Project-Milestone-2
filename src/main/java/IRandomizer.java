// TEAM 8: BARAN ONALAN, LUOSHAN ZHANG, JOEN HO, JULIE MAMMEN
// CPSC 5210
// We labeled any changes we had to do for the purpose of unit testing.
// We also included names next to such changes to indicate who changed them.
//
// BARAN ONALAN
// This is the interface that we created to stub the random behavior in Mastermind.

public interface IRandomizer {
    String generateColorCode();
     int generateID();
}
