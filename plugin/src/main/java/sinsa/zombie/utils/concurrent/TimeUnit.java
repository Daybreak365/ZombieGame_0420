package sinsa.zombie.utils.concurrent;

public enum TimeUnit {
    TICKS(1),
    SECONDS(20),
    MINUTES(1200);

    private final int ticks;

    TimeUnit(int ticks) {
        this.ticks = ticks;
    }

    public int toTicks(int n) {
        return n * this.ticks;
    }

}
