package pl.sda.testing.fortuneWatcher;

import java.math.BigDecimal;

class Notifier {

    void warnAboutStalePrice() {
        System.out.println("EMAIL: The price of gold couldn't be updated");
    }

    void warnAboutLowFortune() {
        System.out.println("EMAIL: YOUR FORTUNE DROPPED BELOW 1 MILLION PLN");
    }

    void notifyAboutFortune(BigDecimal currentFortune) {
        System.out.println("EMAIL: Your gold is worth " + currentFortune);
    }

    public void notifyAboutDroppingPrice(BigDecimal yesterdaysFortune) {
        System.out.println("EMAIL: Your gold was worth more yesterday: " + yesterdaysFortune);
    }
}
