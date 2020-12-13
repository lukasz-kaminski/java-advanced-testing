package pl.sda.testing.fortuneWatcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sda.testing.fortuneWatcher.provider.GoldPriceProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FortuneWatcherTest {
    @Mock
    GoldPriceProvider mockGoldPriceProvider;

    @Mock
    Notifier mockNotifier;

    @InjectMocks
    FortuneWatcher fortuneWatcher;
//    FortuneWatcher fortuneWatcher = new FortuneWatcher(mockGoldPriceProvider, mockNotifier);

    @Test
    void shouldCalculateFortuneAmount() {
        //given
        when(mockGoldPriceProvider.getTodaysPrice())
                .thenReturn(Optional.of(new BigDecimal(1000)));

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
        //when
        fortuneWatcher.assessFortune(Fortune.ofGoldKgs(new BigDecimal(100)));
        //then
        InOrder inOrder = inOrder(mockNotifier);
        inOrder.verify(mockNotifier).warnAboutLowFortune();
        inOrder.verify(mockNotifier).warnAboutStalePrice();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldSendEmailWhenYesterdaysFortuneIsBigger() {
        //given
        when(mockGoldPriceProvider.getPriceForDate(eq(LocalDate.now().minusDays(1L))))
                .thenReturn(Optional.of(new BigDecimal(100)));
        when(mockGoldPriceProvider.getTodaysPrice())
                .thenReturn(Optional.of(new BigDecimal(50)));

        //when
        fortuneWatcher.assessFortune(Fortune.ofGoldKgs(BigDecimal.ONE));
        //then
        verify(mockNotifier).notifyAboutDroppingPrice(eq(new BigDecimal(100)));
    }

    @Test
    void should() {
        //given
        ArgumentCaptor<BigDecimal> captor = ArgumentCaptor.forClass(BigDecimal.class);

        when(mockGoldPriceProvider.getPriceForDate(eq(LocalDate.now().minusDays(1L))))
                .thenReturn(Optional.of(new BigDecimal(100)));
        when(mockGoldPriceProvider.getTodaysPrice())
                .thenReturn(Optional.of(new BigDecimal(50)));

        //when
        fortuneWatcher.assessFortune(Fortune.ofGoldKgs(BigDecimal.ONE));
        //then
        verify(mockNotifier).notifyAboutDroppingPrice(captor.capture());

        assertEquals(new BigDecimal(100), captor.getValue());

    }

}