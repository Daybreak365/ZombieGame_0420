package sinsa.zombie.game;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import sinsa.zombie.config.base.BaseConfig;
import sinsa.zombie.config.text.TextNodes;
import sinsa.zombie.config.text.Texts;

public enum GamePhase {

    PRE_GAME(BarColor.WHITE, BarStyle.SOLID) {
        @Override
        public GamePhase next() {
            return DAYTIME;
        }

        @Override
        public String getTitle(int elapsedTime) {
            return Texts.text(TextNodes.GAME_BOSSBAR_PRE_GAME)
                    .replaceAll(
                            "%d", Integer.toString(BaseConfig.getPhaseLength(this) - elapsedTime)
                    )
                    .replaceAll(
                            "%e", Integer.toString(this.getTotalLeft(elapsedTime))
                    );
            //String.format(Texts.text(TextNodes.GAME_BOSSBAR_PRE_GAME), (BaseConfig.getPhaseLength(this) - elapsedTime));
        }
    },
    DAYTIME(BarColor.GREEN, BarStyle.SOLID) {
        @Override
        public GamePhase next() {
            return SUNSET;
        }

        @Override
        public String getTitle(int elapsedTime) {
            return Texts.text(TextNodes.GAME_BOSSBAR_DAYTIME)
                    .replaceAll(
                            "%d", Integer.toString(BaseConfig.getPhaseLength(this) - elapsedTime)
                    )
                    .replaceAll(
                            "%e", Integer.toString(this.getTotalLeft(elapsedTime))
                    );
        }
    },
    SUNSET(BarColor.YELLOW, BarStyle.SOLID) {
        @Override
        public GamePhase next() {
            return NIGHT;
        }

        @Override
        public String getTitle(int elapsedTime) {
            return Texts.text(TextNodes.GAME_BOSSBAR_SUNSET)
                    .replaceAll(
                            "%d", Integer.toString(BaseConfig.getPhaseLength(this) - elapsedTime)
                    )
                    .replaceAll(
                            "%e", Integer.toString(this.getTotalLeft(elapsedTime))
                    );
        }
    },
    NIGHT(BarColor.RED, BarStyle.SOLID) {
        @Override
        public GamePhase next() {
            return END;
        }

        @Override
        public String getTitle(int elapsedTime) {
            return Texts.text(TextNodes.GAME_BOSSBAR_NIGHT)
                    .replaceAll(
                            "%d", Integer.toString(BaseConfig.getPhaseLength(this) - elapsedTime)
                    )
                    .replaceAll(
                            "%e", Integer.toString(this.getTotalLeft(elapsedTime))
                    );
        }
    },
    END(BarColor.BLUE, BarStyle.SOLID) {
        @Override
        public GamePhase next() {
            return null;
        }

        @Override
        public String getTitle(int elapsedTime) {
            return String.format(Texts.text(TextNodes.GAME_BOSSBAR_END), (BaseConfig.getPhaseLength(this) - elapsedTime));
        }
    };

    private final BarColor barColor;
    private final BarStyle barStyle;

    GamePhase(BarColor barColor, BarStyle barStyle) {
        this.barColor = barColor;
        this.barStyle = barStyle;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public BarStyle getBarStyle() {
        return barStyle;
    }

    public abstract GamePhase next();
    public abstract String getTitle(int elapsedTime);

    public int getLength() {
        return BaseConfig.getPhaseLength(this);
    }

    int getTotalLeft(int elapsedTime) {
        int totalLeft = 0;
        for (int i = this.ordinal(); i < GamePhase.values().length; i++) {
            totalLeft += GamePhase.values()[i].getLength();
        }
        return totalLeft - elapsedTime;
    }

}
