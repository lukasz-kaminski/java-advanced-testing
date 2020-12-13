package pl.sda.testing.fortuneWatcher;

class Notifier {

    void warnAboutStalePrice() {
        System.out.println("EMAIL: The price of gold couldn't be updated");
    }

    void warnAboutLowFortune() {
        System.out.println("EMAIL: YOUR FORTUNE DROPPED BELOW 1 MILLION PLN");
    }
}
