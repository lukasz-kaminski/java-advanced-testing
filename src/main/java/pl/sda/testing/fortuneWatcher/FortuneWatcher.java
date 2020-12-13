package pl.sda.testing.fortuneWatcher;

import pl.sda.testing.fortuneWatcher.provider.GoldPriceProvider;

import java.math.BigDecimal;
import java.util.Optional;

class FortuneWatcher {
    private static final BigDecimal LEAST_ACCEPTABLE_FORTUNE_PLN = new BigDecimal(1_000_000);

    private final GoldPriceProvider goldPriceProvider;
    private final Notifier notifier;

    FortuneWatcher(GoldPriceProvider goldPriceProvider, Notifier notifier) {
        this.goldPriceProvider = goldPriceProvider;
        this.notifier = notifier;
    }

    BigDecimal assessFortune(Fortune fortune) {
        BigDecimal goldPrice;
        Optional<BigDecimal> todaysGoldPrice = goldPriceProvider.getTodaysPrice();
        if(todaysGoldPrice.isEmpty()) {
            goldPrice = goldPriceProvider.getLastAvailableGoldPrice();
            notifier.warnAboutStalePrice();
        } else {
            goldPrice = todaysGoldPrice.get();
        }

        BigDecimal fortuneInPLN = goldPrice.multiply(fortune.getGoldKgs());
        if(fortuneInPLN.compareTo(LEAST_ACCEPTABLE_FORTUNE_PLN) < 0) {
            notifier.warnAboutLowFortune();
        }
        return fortuneInPLN;
    }
}
