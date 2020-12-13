package pl.sda.testing.fortuneWatcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sda.testing.fortuneWatcher.provider.GoldPriceProvider;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FortuneWatcherTest {
    @Mock
    GoldPriceProvider mockGoldPriceProvider;
    @Mock
    Notifier mockNotifier;
    FortuneWatcher fortuneWatcher;

    @Test
    void shouldReturnAssetsValueInPLN() {
        //given
        when(mockGoldPriceProvider.getTodaysPrice()).thenReturn(Optional.of(new BigDecimal(100)));

        fortuneWatcher = new FortuneWatcher(
                Fortune.ofGoldKgs(BigDecimal.TEN),
                mockGoldPriceProvider,
                new Notifier()
        );
        //when
        BigDecimal fortune = fortuneWatcher.assessFortune();
        //then
        assertEquals(new BigDecimal(1000), fortune);
    }

    @Test
    void shouldNotifyOnLowAssetValue() {
        //given
        when(mockGoldPriceProvider.getTodaysPrice()).thenReturn(Optional.of(new BigDecimal(100)));
        fortuneWatcher = new FortuneWatcher(
                Fortune.ofGoldKgs(BigDecimal.TEN),
                mockGoldPriceProvider,
                mockNotifier
        );
        //when
        fortuneWatcher.assessFortune();
        //then
        verify(mockNotifier).warnAboutLowFortune();
        verifyNoMoreInteractions(mockNotifier);
    }
}