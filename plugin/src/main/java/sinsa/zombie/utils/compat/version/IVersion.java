package sinsa.zombie.utils.compat.version;

public interface IVersion {
    int getVersion();
    int getRelease();

    default boolean isAboveOrEqual(IVersion other) {
        return getVersion() >= other.getVersion()
            && (getVersion() != other.getVersion() || getRelease() >= other.getRelease());
    }

    default boolean isBelowOrEqual(IVersion other) {
        return getVersion() <= other.getVersion()
            && (getVersion() != other.getVersion() || getRelease() <= other.getRelease());
    }
}