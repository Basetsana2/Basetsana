package ac.za.cput.factory;

import ac.za.cput.domain.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderFactoryTest {

    @Test
    void testCreateOrder_ValidInput() {
        Order order = OrderFactory.createOrder("Processing", 100.00, "Credit Card");
        assertNotNull(order);
        assertTrue(order.getOrderNumber() > 0);
        assertEquals("Processing", order.getStatus());
        assertEquals("100.0", order.getTotalAmount());
        assertEquals(100.00 * 0.15, order.getTaxAmount(), 0.0001);
        assertEquals("Credit Card", order.getPaymentMethod());
        assertNotNull(order.getOrderDate());
    }

    @Test
    void testCreateOrder_NullStatus() {
        Order order = OrderFactory.createOrder(null, 100.00, "Credit Card");
        assertNull(order);
    }

    @Test
    void testCreateOrder_EmptyStatus() {
        Order order = OrderFactory.createOrder("", 100.00, "Credit Card");
        assertNull(order);
    }

    @Test
    void testCreateOrder_NullPaymentMethod() {
        Order order = OrderFactory.createOrder("Processing", 100.00, null);
        assertNull(order);
    }

    @Test
    void testCreateOrder_EmptyPaymentMethod() {
        Order order = OrderFactory.createOrder("Processing", 100.00, "");
        assertNull(order);
    }

    @Test
    void testCreateOrder_InvalidPaymentMethod() {
        Order order = OrderFactory.createOrder("Processing", 100.00, "Cash");
        assertNull(order);
    }

    @Test
    void testCreateOrder_NegativeTotalAmount() {
        Order order = OrderFactory.createOrder("Processing", -10.00, "Credit Card");
        assertNull(order);
    }

    @Test
    void testCreateOrder_ZeroTotalAmount() {
        Order order = OrderFactory.createOrder("Processing", 0.00, "Credit Card");
        assertNull(order);
    }
}
