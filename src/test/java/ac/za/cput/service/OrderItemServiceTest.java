package ac.za.cput.service;

import ac.za.cput.domain.OrderItem;
import ac.za.cput.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderItemServiceTest {

    private OrderItemRepository repository;
    private OrderItemService service;

    @BeforeEach
    void setUp() {
        repository = mock(OrderItemRepository.class);
        service = new OrderItemService(repository);
    }

    private OrderItem getSampleOrderItem() {
        return new OrderItem.Builder()
                .setId(1L)
                .setQuantity(3)
                .setUnitPrice(20.0)
                .setSubtotal(60.0)
                .setCategory("Scented")
                .build();
    }

    @Test
    void createOrderItem_Success() {
        OrderItem item = getSampleOrderItem();
        when(repository.save(item)).thenReturn(item);

        OrderItem created = service.create(item);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(repository, times(1)).save(item);
    }

    @Test
    void readOrderItem_Found() {
        OrderItem item = getSampleOrderItem();
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        OrderItem found = service.read(1L);

        assertNotNull(found);
        assertEquals(3, found.getQuantity());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void readOrderItem_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        OrderItem found = service.read(99L);

        assertNull(found);
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void updateOrderItem_Exists() {
        OrderItem item = getSampleOrderItem();
        when(repository.existsById(item.getId())).thenReturn(true);
        when(repository.save(item)).thenReturn(item);

        OrderItem updated = service.update(item);

        assertNotNull(updated);
        verify(repository).existsById(item.getId());
        verify(repository).save(item);
    }

    @Test
    void updateOrderItem_NotExists() {
        OrderItem item = getSampleOrderItem();
        when(repository.existsById(item.getId())).thenReturn(false);

        OrderItem updated = service.update(item);

        assertNull(updated);
        verify(repository).existsById(item.getId());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteOrderItem_Exists() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        boolean deleted = service.delete(1L);

        assertTrue(deleted);
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteOrderItem_NotExists() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean deleted = service.delete(99L);

        assertFalse(deleted);
        verify(repository).existsById(99L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getAllOrderItems_ReturnsList() {
        OrderItem item1 = getSampleOrderItem();
        OrderItem item2 = new OrderItem.Builder()
                .setId(2L)
                .setQuantity(5)
                .setUnitPrice(15.0)
                .setSubtotal(75.0)
                .setCategory("Unscented")
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(item1, item2));

        List<OrderItem> items = service.getAll();

        assertNotNull(items);
        assertEquals(2, items.size());
        verify(repository).findAll();
    }

    @Test
    void findByOrderNumber_ReturnsMatchingItems() {
        OrderItem item = getSampleOrderItem();
        when(repository.findByOrder_OrderNumber(123)).thenReturn(List.of(item));

        List<OrderItem> result = service.findByOrderNumber(123);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findByOrder_OrderNumber(123);
    }

    @Test
    void findByCandleNumber_ReturnsMatchingItems() {
        OrderItem item = getSampleOrderItem();
        when(repository.findByCandle_CandleNumber("CAND-001")).thenReturn(List.of(item));

        List<OrderItem> result = service.findByCandleNumber("CAND-001");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findByCandle_CandleNumber("CAND-001");
    }

    @Test
    void calculateSubtotal_CorrectCalculation() {
        double subtotal = service.calculateSubtotal(4, 12.5);
        assertEquals(50.0, subtotal, 0.0001);
    }
}
