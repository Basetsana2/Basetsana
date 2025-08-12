package ac.za.cput.service;

import ac.za.cput.domain.Order;
import ac.za.cput.domain.OrderItem;
import ac.za.cput.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository repository;
    private OrderService service;

    @BeforeEach
    void setUp() {
        repository = mock(OrderRepository.class);
        service = new OrderService(repository);
    }

    private Order getSampleOrder() {
        return new Order.Builder()
                .setOrderNumber(123456)
                .setOrderDate(LocalDate.now())
                .setStatus("Processing")
                .setTotalAmount("150.50")
                .setTaxAmount(22.58)
                .setPaymentMethod("Credit Card")
                // Assuming getOrderItems() is a List<OrderItem>, initialize as needed
                //.setOrderItems(new ArrayList<>())
                .build();
    }

    private OrderItem getSampleOrderItem() {
        return new OrderItem.Builder()
                .setId(9876543210L)
                .setQuantity(2)
                .setUnitPrice(50.25)
                .setSubtotal(100.50)
                .setCategory("Electronics")
                .build();
    }

    @Test
    void createOrder_Success() {
        Order order = getSampleOrder();
        when(repository.save(order)).thenReturn(order);

        Order created = service.create(order);

        assertNotNull(created);
        assertEquals(order.getOrderNumber(), created.getOrderNumber());
        verify(repository, times(1)).save(order);
    }

    @Test
    void readOrder_Found() {
        Order order = getSampleOrder();
        when(repository.findById(123456)).thenReturn(Optional.of(order));

        Order found = service.read(123456);

        assertNotNull(found);
        assertEquals("Processing", found.getStatus());
        verify(repository, times(1)).findById(123456);
    }

    @Test
    void readOrder_NotFound() {
        when(repository.findById(999999)).thenReturn(Optional.empty());

        Order found = service.read(999999);

        assertNull(found);
        verify(repository, times(1)).findById(999999);
    }

    @Test
    void updateOrder_Exists() {
        Order order = getSampleOrder();
        when(repository.existsById(order.getOrderNumber())).thenReturn(true);
        when(repository.save(order)).thenReturn(order);

        Order updated = service.update(order);

        assertNotNull(updated);
        verify(repository).existsById(order.getOrderNumber());
        verify(repository).save(order);
    }

    @Test
    void updateOrder_NotExists() {
        Order order = getSampleOrder();
        when(repository.existsById(order.getOrderNumber())).thenReturn(false);

        Order updated = service.update(order);

        assertNull(updated);
        verify(repository).existsById(order.getOrderNumber());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteOrder_Exists() {
        when(repository.existsById(123456)).thenReturn(true);
        doNothing().when(repository).deleteById(123456);

        boolean result = service.delete(123456);

        assertTrue(result);
        verify(repository).existsById(123456);
        verify(repository).deleteById(123456);
    }

    @Test
    void deleteOrder_NotExists() {
        when(repository.existsById(999999)).thenReturn(false);

        boolean result = service.delete(999999);

        assertFalse(result);
        verify(repository).existsById(999999);
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    void getAllOrders_ReturnsList() {
        Order order1 = getSampleOrder();
        Order order2 = new Order.Builder()
                .setOrderNumber(654321)
                .setOrderDate(LocalDate.now())
                .setStatus("Completed")
                .setTotalAmount("200.00")
                .setTaxAmount(30.0)
                .setPaymentMethod("Debit Card")
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = service.getAll();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(repository).findAll();
    }

    @Test
    void findByStatus_ReturnsMatchingOrders() {
        Order order = getSampleOrder();
        when(repository.findByStatus("Processing")).thenReturn(List.of(order));

        List<Order> result = service.findByStatus("Processing");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Processing", result.get(0).getStatus());
        verify(repository).findByStatus("Processing");
    }

    @Test
    void calculateOrderTotal_OrderExists() {
        Order order = getSampleOrder();
        when(repository.findById(123456)).thenReturn(Optional.of(order));

        double total = service.calculateOrderTotal(123456);

        assertEquals(150.50, total);
        verify(repository).findById(123456);
    }

    @Test
    void calculateOrderTotal_OrderNotFound() {
        when(repository.findById(999999)).thenReturn(Optional.empty());

        double total = service.calculateOrderTotal(999999);

        assertEquals(0.0, total);
        verify(repository).findById(999999);
    }

    @Test
    void addOrderItemToOrder_OrderExists() {
        Order order = getSampleOrder();
        OrderItem orderItem = getSampleOrderItem();

        when(repository.findById(order.getOrderNumber())).thenReturn(Optional.of(order));
        when(repository.save(order)).thenReturn(order);

        // You may need to uncomment and implement add logic for order.getOrderItems().add(orderItem);
        // Here we just test the flow

        Order updatedOrder = service.addOrderItemToOrder(order.getOrderNumber(), orderItem);

        assertNotNull(updatedOrder);
        verify(repository).findById(order.getOrderNumber());
        verify(repository).save(order);
    }

    @Test
    void addOrderItemToOrder_OrderNotFound() {
        OrderItem orderItem = getSampleOrderItem();

        when(repository.findById(999999)).thenReturn(Optional.empty());

        Order updatedOrder = service.addOrderItemToOrder(999999, orderItem);

        assertNull(updatedOrder);
        verify(repository).findById(999999);
        verify(repository, never()).save(any());
    }
}
