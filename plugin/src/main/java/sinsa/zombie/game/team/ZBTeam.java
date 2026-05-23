package sinsa.zombie.game.team;

public enum ZBTeam {
    HUMAN,
    HERO,
    FIRST_ZOMBIE,
    INFECTED,
    SPECTATOR;

    public boolean isHumanSide() {
        return this == HUMAN || this == HERO;
    }

    public boolean isZombieSide() {
        return this == FIRST_ZOMBIE || this == INFECTED;
    }

    public boolean isSameSide(ZBTeam other) {
        if (other == null) return false;
        if (this == SPECTATOR || other == SPECTATOR) return false;
        return (this.isHumanSide() && other.isHumanSide())
                || (this.isZombieSide() && other.isZombieSide());
    }
}