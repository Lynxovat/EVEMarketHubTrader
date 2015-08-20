package pw.redalliance.MarketAPI;

/**
 * Created by Lynx on 22.07.2015.
 */
public class ItemMarketData {
    public double getMaxBye() {
        return maxBye;
    }

    public void setMaxBye(double maxBye) {
        this.maxBye = maxBye;
    }

    public double getMinSell() {
        return minSell;
    }

    public void setMinSell(double minSell) {
        this.minSell = minSell;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public ItemMarketData(double maxBye, double minSell, double volume) {
        this.maxBye = maxBye;
        this.minSell = minSell;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "ItemMarketData{" +
                "maxBye=" + maxBye +
                ", minSell=" + minSell +
                ", volume=" + volume +
                '}';
    }

    private double maxBye;
    private double minSell;
    private double volume;
}
