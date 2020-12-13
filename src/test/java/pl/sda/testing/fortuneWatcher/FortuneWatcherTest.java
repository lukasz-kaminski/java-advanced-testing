package pl.sda.testing.fortuneWatcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import pl.sda.testing.fortuneWatcher.provider.GoldPriceProvider;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FortuneWatcherTest {
    @Mock
    GoldPriceProvider mockGoldPriceProvider;

    @Mock
    Notifier mockNotifier;

    @InjectMocks
    FortuneWatcher fortuneWatcher;

    @Test
    void shouldCalculateFortuneAmount() {
        //given
        when(mockGoldPriceProvider.getTodaysPrice())
                .thenReturn(Optional.of(new BigDecimal(1000)));

        FortuneWatcher fortuneWatcher = new FortuneWatcher(
                mockGoldPriceProvider,
                mockNotifier
        );
        //when
        BigDecimal fortune = fortuneWatcher.assessFortune(Fortune.ofGoldKgs(new BigDecimal(100)));
        //then
        assertEquals(new BigDecimal(100000), fortune);
    }

    @Test
    void shouldSendEmailWhenFortuneIsLow() {
        //given
        when(mockGoldPriceProvider.getTodaysPrice())
                .thenReturn(Optional.of(BigDecimal.ONE));

        FortuneWatcher fortuneWatcher = new FortuneWatcher(
                ,
                mockGoldPriceProvider,
                mockNotifier
        );
        //when
        fortuneWatcher.assessFortune(Fortune.ofGoldKgs(new BigDecimal(100)));
        //then
        verify(mockNotifier).warnAboutLowFortune();
        verifyNoMoreInteractions(mockNotifier);
    }

    @Test
    void shouldNotNotifyWhenFortuneOK() {
        when(mockGoldPriceProvider.getTodaysPrice())
                .thenReturn(Optional.of(new BigDecimal(10000)));

        FortuneWatcher fortuneWatcher = new FortuneWatcher(
                mockGoldPriceProvider,
                mockNotifier
        );
        //when
        fortuneWatcher.assessFortune(Fortune.ofGoldKgs(new BigDecimal(100000)));
        //then
        verifyNoInteractions(mockNotifier);
    }

    @Test
    void shouldSendEmailWhenFortuneIsLowAndPriceIsStale() {
        //given
        when(mockGoldPriceProvider.getTodaysPrice())
                .thenReturn(Optional.empty());

        when(mockGoldPriceProvider.getLastAvailableGoldPrice())
                .thenReturn(BigDecimal.ONE);

        FortuneWatcher fortuneWatcher = new FortuneWatcher(
                mockGoldPriceProvider,
                mockNotifier
        );
        //when
        fortuneWatcher.assessFortune(Fortune.ofGoldKgs(new BigDecimal(100)));
        //then
        verify(mockNotifier).warnAboutLowFortune();
        verify(mockNotifier).warnAboutStalePrice();
        verifyNoMoreInteractions(mockNotifier);
    }

}