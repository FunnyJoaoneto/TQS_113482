package tqs.euromillions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import tqs.euromillions.CuponEuromillions;
import tqs.euromillions.Dip;
import tqs.euromillions.EuromillionsDraw;

public class EuromillionsDrawTest {

    private CuponEuromillions sampleCoupon;

    @BeforeEach
    public void setUp() {
        sampleCoupon = new CuponEuromillions();
        sampleCoupon.appendDip(Dip.generateRandomDip());
        sampleCoupon.appendDip(Dip.generateRandomDip());
        sampleCoupon.appendDip(new Dip(new int[] { 1, 2, 3, 48, 49 }, new int[] { 1, 9 }));
    }

    @DisplayName("reports correct matches in a coupon")
    @Test
    public void testCompareBetWithDrawToGetResults() {
        Dip winningDip, matchesFound;

        // test for full match, using the 3rd dip in the coupon as the Draw results
        assertEquals(sampleCoupon.countDips(), 3);
        winningDip = sampleCoupon.getDipByIndex(2);
        EuromillionsDraw testDraw = new EuromillionsDraw(winningDip);
        matchesFound = testDraw.findMatchesFor(sampleCoupon).get(2);

        assertEquals(winningDip, matchesFound, "expected the bet and the matches found to be equal");

        // test for no matches at all
        testDraw = new EuromillionsDraw(new Dip(new int[] { 9, 10, 11, 12, 13 }, new int[] { 2, 3 }));
        matchesFound = testDraw.findMatchesFor(sampleCoupon).get(2);
        // compare empty with the matches found
        assertEquals(new Dip(), matchesFound);

        // random draw testing
        EuromillionsDraw randomDraw = EuromillionsDraw.generateRandomDraw();
    }

    @Test
    @DisplayName("Test formatted output of CuponEuromillions")
    public void testFormat() {
        CuponEuromillions sampleCoupon1 = new CuponEuromillions();
        sampleCoupon1.appendDip(new Dip(new int[] { 1, 2, 3, 4, 5 }, new int[] { 1, 2 }));
        sampleCoupon1.appendDip(new Dip(new int[] { 10, 20, 30, 40, 50 }, new int[] { 5, 7 }));

        String expectedOutput = "Dip #1:" + new Dip(new int[] { 1, 2, 3, 4, 5 }, new int[] { 1, 2 }).format() + "\n" +
                "Dip #2:" + new Dip(new int[] { 10, 20, 30, 40, 50 }, new int[] { 5, 7 }).format() + "\n";

        assertEquals(expectedOutput, sampleCoupon1.format(), "The formatted output does not match the expected string");
    }

}
