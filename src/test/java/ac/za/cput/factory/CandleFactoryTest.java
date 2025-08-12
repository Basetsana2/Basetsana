package ac.za.cput.factory;

import ac.za.cput.domain.Candle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandleFactoryTest {

    @Test
    void testCreateCandle_ValidInput() {
        Candle candle = CandleFactory.createCandle(
                "Lavender Bliss",
                "Lavender",
                "Purple",
                "Medium",
                25.99,
                10
        );
        assertNotNull(candle);
        assertTrue(candle.getCandleNumber().startsWith("CAND-"));
        assertEquals("Lavender Bliss", candle.getName());
        assertEquals("Lavender", candle.getScent());
        assertEquals("Purple", candle.getColor());
        assertEquals("Medium", candle.getSize());
        assertEquals(25.99, candle.getPrice());
        assertEquals(10, candle.getStockQuantity());
    }

    @Test
    void testCreateCandle_InvalidName() {
        Candle candle = CandleFactory.createCandle(
                "", "Lavender", "Purple", "Medium", 25.99, 10);
        assertNull(candle);
    }

    @Test
    void testCreateCandle_InvalidScent() {
        Candle candle = CandleFactory.createCandle(
                "Lavender Bliss", null, "Purple", "Medium", 25.99, 10);
        assertNull(candle);
    }

    @Test
    void testCreateCandle_InvalidColor() {
        Candle candle = CandleFactory.createCandle(
                "Lavender Bliss", "Lavender", "", "Medium", 25.99, 10);
        assertNull(candle);
    }

    @Test
    void testCreateCandle_InvalidSize() {
        Candle candle = CandleFactory.createCandle(
                "Lavender Bliss", "Lavender", "Purple", null, 25.99, 10);
        assertNull(candle);
    }

    @Test
    void testCreateCandle_NegativePrice() {
        Candle candle = CandleFactory.createCandle(
                "Lavender Bliss", "Lavender", "Purple", "Medium", -1, 10);
        assertNull(candle);
    }

    @Test
    void testCreateCandle_ZeroPrice() {
        Candle candle = CandleFactory.createCandle(
                "Lavender Bliss", "Lavender", "Purple", "Medium", 0, 10);
        assertNull(candle);
    }

    @Test
    void testCreateCandle_NegativeStockQuantity() {
        Candle candle = CandleFactory.createCandle(
                "Lavender Bliss", "Lavender", "Purple", "Medium", 25.99, -5);
        assertNull(candle);
    }
}
