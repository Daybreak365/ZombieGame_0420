package sinsa.zombie.config.text;

import sinsa.zombie.config.cached.Cacher;
import sinsa.zombie.config.cached.Node;

public enum TextNodes implements Node {

    PLUGIN_NAME(
            "plugin_name",
            "좀비 게임"
    ),
    PREFIX(
            "prefix.default",
            "§2《§aZombieGame§2》§f"
    ),
    ERROR_PREFIX(
            "prefix.error",
            "§f[§c!§f] §c"
    ),
    COMMAND_GAME_START(
            "command.game.start",
            "§f관리자 §e%s§f님이 게임을 시작시켰습니다."
    ),
    COMMAND_GAME_STOP(
            "command.game.stop",
            "§f관리자 §e%s§f님이 게임을 중지시켰습니다."
    ),
    ITEM_CHESTPLATE_SURVIVOR_NAME(
            "item.chestplate.survivor.name",
            "§a생존자 흉갑"
    ),
    ITEM_CHESTPLATE_SURVIVOR_LORE(
            "item.chestplate.survivor.lore",
            "설명"
    ),
    ITEM_CHESTPLATE_HERO_NAME(
            "item.chestplate.hero.name",
            "§6영웅 흉갑"
    ),
    ITEM_CHESTPLATE_HERO_LORE(
            "item.chestplate.hero.lore",
            "설명"
    ),
    ITEM_CHESTPLATE_INITIAL_ZOMBIE_NAME(
            "item.chestplate.initial_zombie.name",
            "§c최초 좀비 흉갑"
    ),
    ITEM_CHESTPLATE_INITIAL_ZOMBIE_LORE(
            "item.chestplate.initial_zombie.lore",
            "설명"
    ),
    ITEM_VACCINE_NAME(
            "item.vaccine.name",
            "§2백신"
    ),
    ITEM_VACCINE_LORE(
            "item.vaccine.lore",
            "설명"
    ),
    GAME_RESULT_HUMANS_WIN(
            "game.result.humans_win",
            "§f인간팀 승리!"
    ),
    GAME_RESULT_ZOMBIES_WIN(
            "game.result.zombies_win",
            "§f좀비팀 승리!"
    ),
    GAME_LAST_SURVIVOR_MESSAGE(
            "game.last_survivor.message",
            "§c§l당신은 최후의 생존자 입니다."
    ),
    GAME_VACCINE_USE_MESSAGE(
            "game.vaccine.use.message",
            "§f백신을 사용했습니다."
    ),
    // 【 추가 】
    GAME_VACCINE_USE_BROADCAST(
            "game.vaccine.use.broadcast",
            "§e%t§f님이 §e%p§f님에 의해 백신으로 치료되었습니다."
    ),
    GAME_VACCINE_CURED(
            "game.vaccine.cured",
            "§f백신으로 치료받았습니다!"
    ),
    GAME_VACCINE_NOTICE(
            "game.vaccine.notice",
            "§2§l백신이 배포됐습니다!"
    ),
    GAME_VACCINE_GOT(
            "game.vaccine.got",
            "§2§l백신을 받았습니다."
    ),
    GAME_BOSSBAR_PRE_GAME(
            "game.bossbar.pre_game",
            "§2§l무작위 추첨까지 %d초 남았습니다. (게임 종료까지 %e초)"
    ),
    GAME_BOSSBAR_DAYTIME(
            "game.bossbar.daytime",
            "§2§l낮 시간 (%d초) (게임 종료까지 %e초)"
    ),
    GAME_BOSSBAR_SUNSET(
            "game.bossbar.sunset",
            "§e§l해질녘 시간 (%d초) (게임 종료까지 %e초)"
    ),
    GAME_BOSSBAR_NIGHT(
            "game.bossbar.night",
            "§c§l밤 시간 (%d초) (게임 종료까지 %e초)"
    ),
    GAME_BOSSBAR_END(
            "game.bossbar.end",
            "§2§l게임 종료까지 %d초 남았습니다."
    ),
    GAME_BUFF_LAST_HUMAN_MESSAGE(
            "game.buff.last_human.message",
            "§2§l인간이 한 명 남아 이동 속도 증가 효과를 받습니다!"
    ),
    // 【 추가 】
    GAME_BUFF_LAST_HUMAN_BROADCAST(
            "game.buff.last_human.broadcast",
            "§e%s§f님이 최후의 생존자가 되었습니다."
    ),
    GAME_FIRST_KILL(
            "game.first.kill",
            "§c§l사람이 좀비를 처음 죽었습니다! 처치자: %k, 죽은 좀비: %s"
    ),
    GAME_FIRST_DEATH(
            "game.first.death",
            "§c§l좀비가 사람을 처음 감염했습니다! 감염자: %k, 감염된 사람: %s"
    ),
    GAME_HERO_INFECTED(
            "game.hero.infected",
            "§c§l%k 에 의해 영웅 %s님이 감염되었습니다."
    ),
    GAME_NO_TEAMKILL(
            "game.no_teamkill",
            "§c§l같은 팀을 공격할 수 없습니다!"
    ),
    // 【 추가 】
    // Bukkit.broadcastMessage("§2§l가장 많은 " + name + KoreanUtil.getJosa(name, KoreanUtil.Josa.을를) + " 죽인 플레이어§f§l(" + maxValue + "킬)§8§l: " + joiner);
    GAME_END_STATS_BROADCAST(
            "game.end.broadcast",
            "§2§l가장 많은 %t 죽인 플레이어§f§l(%d킬)§8§l: %p"
    );

    private final String path;
    private final Object defaultValue;
    private final Cacher nodeHandler;
    private final String[] comments;

    TextNodes(String path, Object defaultValue, Cacher nodeHandler, String... comments) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.nodeHandler = nodeHandler;
        this.comments = comments;
    }

    TextNodes(String path, Object defaultValue, String... comments) {
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
