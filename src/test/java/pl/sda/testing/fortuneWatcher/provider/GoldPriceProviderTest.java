package pl.sda.testing.fortuneWatcher.provider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoldPriceProviderTest {
    @Mock
    HttpClient httpClient;

    @Test
    void shouldRespondWithTodaysPrice() throws IOException, InterruptedException {
        //given
        GoldPriceProvider goldPriceProvider = new GoldPriceProvider(httpClient);
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.body()).thenReturn("[{\"cena\": \"100\"}]");
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(response);
        //when
        Optional<BigDecimal> todaysPrice = goldPriceProvider.getTodaysPrice();
        //then
        assertTrue(todaysPrice.isPresent());
        assertEquals(new BigDecimal(100), todaysPrice.get());
    }
}