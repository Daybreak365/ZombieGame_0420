package sinsa.zombie.config.command;

import sinsa.zombie.config.cached.Cacher;
import sinsa.zombie.config.cached.Node;

import java.util.ArrayList;

public enum CommandNodes implements Node {

    GAME_END_HUMANS_WIN(
            "game_end.humans_win",
            new ArrayList<String>()
    ),
    GAME_END_ZOMBIES_WIN(
            "game_end.zombies_win",
            new ArrayList<String>()
    ),
    LAST_SURVIVOR(
            "last_survivor",
            new ArrayList<String>()
    ),
    BEFORE_GAME_END_15_SECONDS(
            "before_game_end_15_seconds",
            new ArrayList<String>()
    );

    private final String path;
    private final Object defaultValue;
    private final Cacher nodeHandler;
    private final String[] comments;

    CommandNodes(String path, Object defaultValue, Cacher nodeHandler, String... comments) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.nodeHandler = nodeHandler;
        this.comments = comments;
    }

    CommandNodes(String path, Object defaultValue, String... comments) {
        this(path, defaultValue, null, comments);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Object getDefault() {
        return defaultValue;
    }

    @Override
    public boolean hasCacher() {
        return nodeHandler != null;
    }

    @Override
    public Cacher getCacher() {
        return nodeHandler;
    }

    @Override
    public String[] getComments() {
        return comments;
    }
}
