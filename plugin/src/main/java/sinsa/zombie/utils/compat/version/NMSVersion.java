package sinsa.zombie.utils.compat.version;

public enum NMSVersion implements IVersion {
    UNSUPPORTED(-1, -1),
    v1_21_R7(21, 7);

    private final int version;
    private final int release;

    NMSVersion(int version, int release) {
        this.version = version;
        this.release = release;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public int getRelease() {
        return release;
    }
}