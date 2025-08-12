package ac.za.cput.controller;

import ac.za.cput.domain.Invoice;
import ac.za.cput.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Order order;

    @BeforeEach
    void setUp() {
        order = buildOrder(101, LocalDate.of(2025, 8, 5), "Pending", "15000.00", 1500.00, "Card");
    }

    // Helper method
    private Order buildOrder(int orderNumber, LocalDate orderDate, String status, String totalAmount, double taxAmount, String paymentMethod) {
        return new Order.Builder()
                .setOrderNumber(orderNumber)
                .setOrderDate(orderDate)
                .setStatus(status)
                .setTotalAmount(totalAmount)
                .setTaxAmount(taxAmount)
                .setPaymentMethod(paymentMethod)
                .setInvoice(new Invoice())
                .build();
    }

    @Test
    void testCreateOrder() {
        ResponseEntity<Order> response = restTemplate.postForEntity("/order/create", order, Order.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
       // assertEquals(order.getOrderNumber(), response.getBody().getOrderNumber());
    }

    @Test
    void testReadOrder() {
        // Create first
        restTemplate.postForEntity("/order/create", order, Order.class);

        ResponseEntity<Order> response = restTemplate.getForEntity("/order/read/101", Order.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order.getOrderNumber(), response.getBody().getOrderNumber());
    }

    @Test
    void testUpdateOrder() {
        restTemplate.postForEntity("/order/create", order, Order.class);

        Order updatedOrder = buildOrder(101, LocalDate.of(2025, 8, 6), "Completed", "15000.00", 1500.00, "EFT");
        HttpEntity<Order> requestEntity = new HttpEntity<>(updatedOrder);

        ResponseEntity<Order> response = restTemplate.exchange("/order/update", HttpMethod.PUT, requestEntity, Order.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Completed", response.getBody().getStatus());
    }

    @Test
    void testFindById() {
        restTemplate.postForEntity("/order/create", order, Order.class);

        ResponseEntity<Order> response = restTemplate.getForEntity("/order/find/101", Order.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order.getOrderNumber(), response.getBody().getOrderNumber());
    }

    @Test
    void testGetAllOrders() {
        restTemplate.postForEntity("/order/create", order, Order.class);
        restTemplate.postForEntity("/order/create",
                buildOrder(102, LocalDate.of(2025, 8, 4), "Completed", "8000.00", 800.00, "Cash"), Order.class);

        ResponseEntity<Order[]> response = restTemplate.getForEntity("/order/all", Order[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Order> orders = Arrays.asList(response.getBody());
        assertTrue(orders.size() >= 2);
    }
}
