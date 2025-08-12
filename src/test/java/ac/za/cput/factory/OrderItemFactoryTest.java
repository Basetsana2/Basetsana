package ac.za.cput.factory;

import ac.za.cput.domain.Candle;
import ac.za.cput.domain.Order;
import ac.za.cput.domain.OrderItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemFactoryTest {

    // Helper method to create a valid Candle instance
    private Candle createValidCandle() {
        return new Candle.Builder()
                .setCandleNumber("CAND-123456")
                .setName("Vanilla")
                .setScent("Vanilla")
                .setColor("White")
                .setSize("Small")
                .setPrice(15.99)
                .setStockQuantity(100)
                .build();
    }

    // Helper method to create a valid Order instance
    private Order createValidOrder() {
        return new Order.Builder()
                .setOrderNumber(123456)
                .setOrderDate(java.time.LocalDate.now())
                .setStatus("Processing")
                .setTotalAmount("100.00")
                .setTaxAmount(15.00)
                .setPaymentMethod("Credit Card")
                .build();
    }

    @Test
    void testCreateOrderItem_ValidInput() {
        Candle candle = createValidCandle();
        Order order = createValidOrder();

        OrderItem orderItem = OrderItemFactory.createOrderItem(2, 15.99, "Scented", candle, order);

        assertNotNull(orderItem);
        assertTrue(orderItem.getId() > 0);
        assertEquals(2, orderItem.getQuantity());
        assertEquals(15.99, orderItem.getUnitPrice());
        assertEquals(2 * 15.99, orderItem.getSubtotal());
        assertEquals("Scented", orderItem.getCategory());
    }

    @Test
    void testCreateOrderItem_QuantityZero() {
        Candle candle = createValidCandle();
        Order order = createValidOrder();

        OrderItem orderItem = OrderItemFactory.createOrderItem(0, 15.99, "Scented", candle, order);
        assertNull(orderItem);
    }

    @Test
    void testCreateOrderItem_QuantityNegative() {
        Candle candle = createValidCandle();
        Order order = createValidOrder();

        OrderItem orderItem = OrderItemFactory.createOrderItem(-1, 15.99, "Scented", candle, order);
        assertNull(orderItem);
    }

    @Test
    void testCreateOrderItem_UnitPriceZero() {
        Candle candle = createValidCandle();
        Order order = createValidOrder();

        OrderItem orderItem = OrderItemFactory.createOrderItem(2, 0.0, "Scented", candle, order);
        assertNull(orderItem);
    }

    @Test
    void testCreateOrderItem_UnitPriceNegative() {
        Candle candle = createValidCandle();
        Order order = createValidOrder();

        OrderItem orderItem = OrderItemFactory.createOrderItem(2, -5.0, "Scented", candle, order);
        assertNull(orderItem);
    }

    @Test
    void testCreateOrderItem_CategoryNullOrEmpty() {
        Candle candle = createValidCandle();
        Order order = createValidOrder();

        assertNull(OrderItemFactory.createOrderItem(2, 15.99, null, candle, order));
        assertNull(OrderItemFactory.createOrderItem(2, 15.99, "", candle, order));
    }

    @Test
    void testCreateOrderItem_CandleNull() {
        Order order = createValidOrder();
        OrderItem orderItem = OrderItemFactory.createOrderItem(2, 15.99, "Scented", null, order);
        assertNull(orderItem);
    }

    @Test
    void testCreateOrderItem_OrderNull() {
        Candle candle = createValidCandle();
        OrderItem orderItem = OrderItemFactory.createOrderItem(2, 15.99, "Scented", candle, null);
        assertNull(orderItem);
    }

    @Test
    void testCreateOrderItem_UnitPriceMismatch() {
        Candle candle = createValidCandle();
        Order order = createValidOrder();

        // Candle price is 15.99, pass different price to unitPrice
        OrderItem orderItem = OrderItemFactory.createOrderItem(2, 20.00, "Scented", candle, order);
        assertNull(orderItem);
    }
}
