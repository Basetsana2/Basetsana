package ac.za.cput.service;

import ac.za.cput.domain.Candle;
import ac.za.cput.repository.CandleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CandleServiceTest {

    private CandleRepository repository;
    private CandleService service;

    @BeforeEach
    void setUp() {
        repository = mock(CandleRepository.class);
        service = new CandleService(repository);
    }

    private Candle getSampleCandle() {
        return new Candle.Builder()
                .setCandleNumber("CAND-123")
                .setName("Vanilla")
                .setScent("Vanilla")
                .setColor("White")
                .setSize("Small")
                .setPrice(15.99)
                .setStockQuantity(20)
                .build();
    }

    @Test
    void createCandle_Success() {
        Candle candle = getSampleCandle();
        when(repository.save(candle)).thenReturn(candle);

        Candle created = service.create(candle);

        assertNotNull(created);
        assertEquals("CAND-123", created.getCandleNumber());
        verify(repository, times(1)).save(candle);
    }

    @Test
    void readCandle_Found() {
        Candle candle = getSampleCandle();
        when(repository.findById("CAND-123")).thenReturn(Optional.of(candle));

        Candle found = service.read("CAND-123");

        assertNotNull(found);
        assertEquals("Vanilla", found.getScent());
        verify(repository, times(1)).findById("CAND-123");
    }

    @Test
    void readCandle_NotFound() {
        when(repository.findById("CAND-999")).thenReturn(Optional.empty());

        Candle found = service.read("CAND-999");

        assertNull(found);
        verify(repository, times(1)).findById("CAND-999");
    }

    @Test
    void updateCandle_Exists() {
        Candle candle = getSampleCandle();
        when(repository.existsById(candle.getCandleNumber())).thenReturn(true);
        when(repository.save(candle)).thenReturn(candle);

        Candle updated = service.update(candle);

        assertNotNull(updated);
        verify(repository).existsById(candle.getCandleNumber());
        verify(repository).save(candle);
    }

    @Test
    void updateCandle_NotExists() {
        Candle candle = getSampleCandle();
        when(repository.existsById(candle.getCandleNumber())).thenReturn(false);

        Candle updated = service.update(candle);

        assertNull(updated);
        verify(repository).existsById(candle.getCandleNumber());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteCandle_Exists() {
        when(repository.existsById("CAND-123")).thenReturn(true);
        doNothing().when(repository).deleteById("CAND-123");

        boolean result = service.delete("CAND-123");

        assertTrue(result);
        verify(repository).existsById("CAND-123");
        verify(repository).deleteById("CAND-123");
    }

    @Test
    void deleteCandle_NotExists() {
        when(repository.existsById("CAND-999")).thenReturn(false);

        boolean result = service.delete("CAND-999");

        assertFalse(result);
        verify(repository).existsById("CAND-999");
        verify(repository, never()).deleteById(anyString());
    }

    @Test
    void getAll_ReturnsList() {
        Candle candle1 = getSampleCandle();
        Candle candle2 = new Candle.Builder()
                .setCandleNumber("CAND-124")
                .setName("Lavender")
                .setScent("Lavender")
                .setColor("Purple")
                .setSize("Medium")
                .setPrice(18.99)
                .setStockQuantity(15)
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(candle1, candle2));

        List<Candle> candles = service.getAll();

        assertNotNull(candles);
        assertEquals(2, candles.size());
        verify(repository).findAll();
    }

    @Test
    void findByScent_ReturnsMatching() {
        Candle candle = getSampleCandle();
        when(repository.findByScent("Vanilla")).thenReturn(List.of(candle));

        List<Candle> result = service.findByScent("Vanilla");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Vanilla", result.get(0).getScent());
        verify(repository).findByScent("Vanilla");
    }

    @Test
    void findByPriceRange_ReturnsMatching() {
        Candle candle = getSampleCandle();
        when(repository.findByPriceBetween(10.0, 20.0)).thenReturn(List.of(candle));

        List<Candle> result = service.findByPriceRange(10.0, 20.0);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getPrice() >= 10.0 && result.get(0).getPrice() <= 20.0);
        verify(repository).findByPriceBetween(10.0, 20.0);
    }

    @Test
    void checkStockQuantity_CandleExists() {
        Candle candle = getSampleCandle();
        when(repository.findById("CAND-123")).thenReturn(Optional.of(candle));

        int stock = service.checkStockQuantity("CAND-123");

        assertEquals(20, stock);
        verify(repository).findById("CAND-123");
    }

    @Test
    void checkStockQuantity_CandleNotExists() {
        when(repository.findById("CAND-999")).thenReturn(Optional.empty());

        int stock = service.checkStockQuantity("CAND-999");

        assertEquals(0, stock);
        verify(repository).findById("CAND-999");
    }
}
