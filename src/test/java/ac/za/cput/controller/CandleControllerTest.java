package ac.za.cput.controller;

import ac.za.cput.domain.Candle;
import ac.za.cput.service.ICandleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CandleController.class)  // Loads only the controller, not the entire app
public class CandleControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Used to simulate HTTP requests

    @MockBean
    private ICandleService candleService;  // Mock the service layer

    private Candle testCandle;

    @BeforeEach
    void setUp() {
        testCandle = new Candle.Builder()
                .setCandleNumber("1")
                .setName("Vanilla")
                .setPrice(15.99)
                .setSize("Medium")
                .build();
    }

    @Test
    void createCandle_ShouldReturnCreatedCandle() throws Exception {
        Mockito.when(candleService.create(testCandle)).thenReturn(testCandle);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/candle/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(testCandle))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.candleNumber").value("1"))
                .andDo(print());  // Print request/response for debugging
    }

    @Test
    void getCandleById_ShouldReturnCandle() throws Exception {
        Mockito.when(candleService.read("1")).thenReturn(testCandle);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/candle/read/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.candleNumber").value("1"))
                .andDo(print());
    }

    @Test
    void updateCandle_ShouldReturnUpdatedCandle() throws Exception {
        Mockito.when(candleService.update(testCandle)).thenReturn(testCandle);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/candle/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(testCandle))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.candleNumber").value("1"))
                .andDo(print());
    }

    @Test
    void getAllCandles_ShouldReturnList() throws Exception {
        List<Candle> candleList = Collections.singletonList(testCandle);
        Mockito.when(candleService.getAll()).thenReturn(candleList);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/candle/all")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    void getCandleById_ShouldReturnNotFound() throws Exception {
        Mockito.when(candleService.read("999")).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/candle/read/999")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }
}