package pl.sda.testing.fortuneWatcher.provider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoldPriceProviderTest {
    @Mock
    HttpClient httpClient;
    
    @InjectMocks
    GoldPriceProvider goldPriceProvider;

    @Test
    void shouldReadPriceFromServerResponse() throws IOException, InterruptedException {
        //given
        HttpResponse response = mock(HttpResponse.class);
        when(response.body()).thenReturn("[{\"cena\": \"100\"}]");

        when(httpClient.send(any(), any())).thenReturn(response);

        //when
        Optional<BigDecimal> todaysPrice = goldPriceProvider.getTodaysPrice();

        //then
        assertTrue(todaysPrice.isPresent());
        assertEquals(new BigDecimal(100), todaysPrice.get());
    }

}