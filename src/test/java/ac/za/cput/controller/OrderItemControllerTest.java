package ac.za.cput.controller;

import ac.za.cput.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderItemControllerTest.java
 * Test for OrderItemController (create, read, update, findById only) using TestRestTemplate
 * Author: Basetsana Masisi
 * Student Number: 222309385
 * Date: 05 August 2025
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderItemControllerTest {



    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" +  "/order-item";
        orderItem = new OrderItem.Builder()
                .setId(null) // Let the database generate ID
                .setQuantity(3)
                .setUnitPrice(100.00)
                .setSubtotal(300.00)
                .setCategory("Candle")
                .build();
    }

    @Test
    void createOrderItem() {
        ResponseEntity<OrderItem> response = restTemplate.postForEntity(baseUrl + "/create", orderItem, OrderItem.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(orderItem.getQuantity(), response.getBody().getQuantity());
    }

    @Test
    void readOrderItem() {
        // First create the OrderItem
        OrderItem created = restTemplate.postForEntity(baseUrl + "/create", orderItem, OrderItem.class).getBody();
        assertNotNull(created);
        assertNotNull(created.getId());

        // Then read it by ID
        ResponseEntity<OrderItem> response = restTemplate.getForEntity(baseUrl + "/read/" + created.getId(), OrderItem.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(created.getId(), response.getBody().getId());
    }

    @Test
    void updateOrderItem() {
        // Create first
        OrderItem created = restTemplate.postForEntity(baseUrl + "/create", orderItem, OrderItem.class).getBody();
        assertNotNull(created);
        assertNotNull(created.getId());

        // Update quantity and subtotal
        OrderItem updatedOrderItem = new OrderItem.Builder()
                .copy(created)
                .setQuantity(5)
                .setSubtotal(500.00)
                .build();

        HttpEntity<OrderItem> entity = new HttpEntity<>(updatedOrderItem);

        ResponseEntity<OrderItem> response = restTemplate.exchange(baseUrl + "/update", HttpMethod.PUT, entity, OrderItem.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getQuantity());
        assertEquals(500.00, response.getBody().getSubtotal());
    }

    @Test
    void findByIdOrderItem() {
        // Create first
        OrderItem created = restTemplate.postForEntity(baseUrl + "/create", orderItem, OrderItem.class).getBody();
        assertNotNull(created);
        assertNotNull(created.getId());

        // Find by ID
        ResponseEntity<OrderItem> response = restTemplate.getForEntity(baseUrl + "/find/" + created.getId(), OrderItem.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(created.getId(), response.getBody().getId());
    }
}
