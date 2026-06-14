package sinsa.zombie.game;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sinsa.zombie.ZombieGame;
import sinsa.zombie.config.base.BaseConfig;
import sinsa.zombie.config.base.BaseNodes;
import sinsa.zombie.config.base.serializable.SpawnLocation;
import sinsa.zombie.config.command.CommandConfig;
import sinsa.zombie.config.command.CommandNodes;
import sinsa.zombie.config.kit.KitNodes;
import sinsa.zombie.config.kit.Kits;
import sinsa.zombie.config.text.TextNodes;
import sinsa.zombie.config.text.Texts;
import sinsa.zombie.stat.StatView;
import sinsa.zombie.stat.Stats;
import sinsa.zombie.utils.compat.MaterialX;
import sinsa.zombie.utils.concurrent.SimpleTimer;
import sinsa.zombie.utils.concurrent.TimeUnit;
import sinsa.zombie.utils.language.korean.KoreanUtil;
import sinsa.zombie.utils.minecraft.item.ItemBuilder;
import sinsa.zombie.utils.random.Random;

import java.util.*;

public class Game extends SimpleTimer implements Listener {

    public static final int TIME_NIGHT = 14000;
    public static final int TIME_DAY = 6000;




    public static PotionEffect NIGHT_VISION = new PotionEffect(PotionEffectType.NIGHT_VISION, 500, 0, false, false);
    private static final NamespacedKey ZOMBIE_SPEED_KEY = NamespacedKey.minecraft("ZOMBIE_SPEED");
    private static final NamespacedKey ZOMBIE_DAMAGE_KEY = NamespacedKey.minecraft("ZOMBIE_DAMAGE");
    private static final NamespacedKey HUMAN_SPEED_KEY = NamespacedKey.minecraft("HUMAN_SPEED");
    private static final NamespacedKey HUMAN_DAMAGE_KEY = NamespacedKey.minecraft("HUMAN_DAMAGE");
    private final Random random = new Random();
    public final AttributeModifier MODIFIER_SPEED = new AttributeModifier(ZOMBIE_SPEED_KEY, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_SPEED) / 100.0, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);
    public final AttributeModifier MODIFIER_DAMAGE = new AttributeModifier(ZOMBIE_DAMAGE_KEY, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_DAMAGE) / 100.0, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);
    public final AttributeModifier MODIFIER_HUMAN_SPEED = new AttributeModifier(HUMAN_SPEED_KEY, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_SPEED) / 100.0, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);
    public final AttributeModifier MODIFIER_HUMAN_DAMAGE = new AttributeModifier(HUMAN_DAMAGE_KEY, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_DAMAGE) / 100.0, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);
    private final MostKills MOST_KILL_ZOMBIE = new MostKills("좀비"), MOST_KILL_HUMAN = new MostKills("인간");




    private final Map<UUID, Participant> participants = new HashMap<>();
    private GamePhase currentPhase = GamePhase.PRE_GAME;
    private final BossBar bossBar = Bukkit.createBossBar("", currentPhase.getBarColor(), currentPhase.getBarStyle());
    private int initialSurvivors = 0;
    private boolean vaccineDistributed = false;
    private boolean firstKill = false, firstDeath = false;
    private boolean lastHumanBuff = false;
    private boolean beforeEnd15SecondCommandsExecuted = false;
    private boolean isGameEnd = false;
    private Location spawn;
    private final Map<UUID, Integer> humanKillCounts = new HashMap<>();
    private final Map<UUID, Integer> zombieKillCounts = new HashMap<>();
    private final Set<UUID> pendingResetPlayers = new HashSet<>();
    private final Set<UUID> pendingInfecteeKitPlayers = new HashSet<>();
    private boolean suppressStateChecks = false;

    private class MostKills {

        private final String name;
        private final Map<Participant, Integer> kills = new HashMap<>();

        private MostKills(final String name) {
            this.name = name;
        }

        private void addKills(Participant participant) {
            kills.put(participant, kills.getOrDefault(participant, 0) + 1);
        }

        private void print() {
            final List<Participant> most = new ArrayList<>();
            if (kills.isEmpty()) return;
            int maxValue = Collections.max(kills.values());
            for (Map.Entry<Participant, Integer> entry : kills.entrySet()) {
                if (entry.getValue() == maxValue) {
                    most.add(entry.getKey());
                }
            }
            final StringJoiner joiner = new StringJoiner("§e, ");
            for (Participant participant : most) {
                joiner.add("§a" + participant.getPlayer().getName());
            }
            // Bukkit.broadcastMessage("§2§l가장 많은 " + name + KoreanUtil.getJosa(name, KoreanUtil.Josa.을를) + " 죽인 플레이어§f§l(" + maxValue + "킬)§8§l: " + joiner);
            Bukkit.broadcastMessage(
                    Texts.text(TextNodes.GAME_END_STATS_BROADCAST)
                            .replaceAll("%t", name + KoreanUtil.getJosa(name, KoreanUtil.Josa.을를))
                            .replaceAll("%d", Integer.toString(maxValue))
                            .replaceAll("%p", joiner.toString())

            );
        }

    }

    public final EnumMap<PlayerRole, Team> scoreboardTeams = new EnumMap<>(PlayerRole.class);

    public Game() {
        super(TaskType.INFINITE, -1);
        setPeriod(TimeUnit.TICKS, 1);
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (PlayerRole role : PlayerRole.values()) {
            final Team newTeam = mainScoreboard.registerNewTeam(UUID.randomUUID().toString());
            newTeam.setColor(ChatColor.getByChar(role.getNameColor().charAt(1)));
            scoreboardTeams.put(role, newTeam);
        }
    }

    private void endGame() {
        this.isGameEnd = true;
        snapshotApiState();
        // 【 추가 】 모든 인간이 좀비로 감염되면, 게임이 즉시 종료되고 모든 사람의 아이템이 제거되는 기능 추가
        GameManager.stop();
        for (Participant participant : getParticipants()) {
            resetParticipantAfterGame(participant);
        }
        bossBar.removeAll();
    }

    @Override
    protected void run(int count) {
        if (isGameEnd) return;
        switch(currentPhase) {
            case PRE_GAME:
                if (count == 1) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        bossBar.addPlayer(player);
                        participants.put(player.getUniqueId(), new Participant(player));
                    }
                    if (getParticipants().size() < 5) {
                        Bukkit.broadcastMessage("§c§l참가자 수가 부족해 게임이 종료되었습니다. §7§l(최소 5명)");
                        stop();
                        return;
                    }
                    final SpawnLocation nextLocation = BaseConfig.SpawnLocations.getNextSpawn();
                    if (nextLocation != null) {
                        final Location nextLoc = nextLocation.toBukkitLocation();
                        this.spawn = nextLoc;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.teleport(nextLoc);
                        }
                    } else {
                        final List<SpawnLocation> locations = new ArrayList<>(BaseConfig.SpawnLocations.getLocations());
                        if (!locations.isEmpty()) {
                            final Location loc = random.pick(locations).toBukkitLocation();
                            this.spawn = loc;
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.teleport(loc);
                            }
                        } else {
                            Bukkit.broadcastMessage("§c§l설정된 스폰 지점이 없어 게임이 종료되었습니다.");
                            stop();
                            return;
                        }
                    }
                    Bukkit.getPluginManager().registerEvents(this, ZombieGame.getInstance());
                }
                break;
            case DAYTIME:
                if (count == 1) {
                    int zombieCount = 0;
                    suppressStateChecks = true;
                    try {
                        final List<Participant> pList = new ArrayList<>(getParticipantsOnline());
                        for (int i = 0; i < BaseConfig.getInt(BaseNodes.AMOUNT_HERO); i++) {
                            if (pList.isEmpty()) break;
                            final int sel = random.nextInt(pList.size());
                            final Participant selP = pList.remove(sel);
                            selP.setRole(PlayerRole.HERO, true, false);
                            StatView.get(selP.getPlayer()).addValue(Stats.HERO_LANDING, 1);
                            initialSurvivors++;
                        }
                        for (int i = 0; i < BaseConfig.getInt(BaseNodes.AMOUNT_INITIAL_ZOMBIE); i++) {
                            if (pList.isEmpty()) break;
                            final int sel = random.nextInt(pList.size());
                            final Participant selP = pList.remove(sel);
                            selP.setRole(PlayerRole.INITIAL_ZOMBIE, true, false);
                            StatView.get(selP.getPlayer()).addValue(Stats.CHOSEN_GENE, 1);
                            zombieCount++;
                        }
                        initialSurvivors += pList.size();
                        for (Participant left : pList) {
                            left.setRole(PlayerRole.SURVIVOR, true, false);
                        }
                    } finally {
                        suppressStateChecks = false;
                    }
                    //Bukkit.broadcastMessage("인간팀 " + initialSurvivors + "명, 좀비팀 " + zombieCount + "명");
                    if (zombieCount == 0 || initialSurvivors == 0) {
                        Bukkit.broadcastMessage("§c§l좀비팀 또는 인간팀 중 구성원이 0명인 팀이 있어 게임이 종료되었습니다.");
                        stop();
                        return;
                    }

                    for (Participant participant : getParticipantsOnline()) {
                        giveRoleKit(participant, participant.role);
                    }
                    checkState();
                }
                break;
        }

        if (!beforeEnd15SecondCommandsExecuted && getTotalRemainingTime() <= 15) {
            beforeEnd15SecondCommandsExecuted = true;
            CommandConfig.dispatch(CommandNodes.BEFORE_GAME_END_15_SECONDS);
        }

        snapshotApiState();

        final int max = BaseConfig.getPhaseLength(currentPhase) * 20;
        bossBar.setProgress(((double) max - count) / ((double) max));
        bossBar.setTitle(currentPhase.getTitle(getElapsedTime()));
        if (count >= BaseConfig.getPhaseLength(currentPhase) * 20) {
            this.currentPhase = currentPhase.next();

            spawn.getWorld().setTime(currentPhase == GamePhase.NIGHT ? TIME_NIGHT : TIME_DAY);

            if (currentPhase != null) {
                bossBar.setColor(currentPhase.getBarColor());
                bossBar.setStyle(currentPhase.getBarStyle());
                setCount(0);
            } else {
                final List<Participant> humans = getHumans();
                for (Participant human : humans) {
                    StatView.get(human.getPlayer()).addValue(Stats.LEGENDARY, 1);
                    if (human.role == PlayerRole.HERO) {
                        StatView.get(human.getPlayer()).addValue(Stats.HOPE_OF_HUMANS, 1);
                    }
                }
                Bukkit.broadcastMessage(Texts.text(TextNodes.GAME_RESULT_HUMANS_WIN));
                CommandConfig.dispatch(CommandNodes.GAME_END_HUMANS_WIN);
                MOST_KILL_ZOMBIE.print();
                MOST_KILL_HUMAN.print();
                endGame();
            }
        }
    }



    @EventHandler
    private void onInventoryClick(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            final Player clicker = (Player) e.getWhoClicked();
            if (isParticipating(clicker.getUniqueId()) && e.getSlotType() == InventoryType.SlotType.ARMOR) {
                e.setCancelled(true);
                clicker.sendMessage("§c§l게임 진행 중 갑옷을 임의로 벗을 수 없습니다!");
            }
        }
    }

    public int getElapsedTime() {
        return getCount() / 20;
    }

    public int getTotalGameTime() {
        int total = 0;
        for (GamePhase phase : GamePhase.values()) {
            total += BaseConfig.getPhaseLength(phase);
        }
        return total;
    }

    public int getTotalElapsedTime() {
        if (currentPhase == null) return getTotalGameTime();
        int elapsed = getElapsedTime();
        for (int i = 0; i < currentPhase.ordinal(); i++) {
            elapsed += BaseConfig.getPhaseLength(GamePhase.values()[i]);
        }
        return Math.min(elapsed, getTotalGameTime());
    }

    public int getTotalRemainingTime() {
        return Math.max(0, getTotalGameTime() - getTotalElapsedTime());
    }

    public int getInitialHumans() {
        return initialSurvivors;
    }

    public List<Participant> getHumans() {
        final List<Participant> humans = new ArrayList<>();
        for (Participant participant : getParticipantsOnline()) {
            if (participant.role.isHuman() && participant.role != PlayerRole.DEFAULT) humans.add(participant);
        }
        return humans;
    }

    public int getHumanCount() {
        int count = 0;
        for (Participant participant : getParticipantsOnline()) {
            if (participant.role.isHuman() && participant.role != PlayerRole.DEFAULT) count++;
        }
        return count;
    }

    public int getZombieCount() {
        int count = 0;
        for (Participant participant : getParticipantsOnline()) {
            if (participant.role.isZombie()) count++;
        }
        return count;
    }

    public int getHumanKillCount(UUID uuid) {
        return humanKillCounts.getOrDefault(uuid, 0);
    }

    public int getZombieKillCount(UUID uuid) {
        return zombieKillCounts.getOrDefault(uuid, 0);
    }


    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        bossBar.addPlayer(player);
        if (pendingResetPlayers.remove(player.getUniqueId())) {
            resetPlayerAfterGame(player);
            GameManager.clearPendingReset(player.getUniqueId());
        }
        if (isParticipating(player.getUniqueId())) {
            final Participant participant = getParticipant(player);
            if (pendingInfecteeKitPlayers.remove(player.getUniqueId())) {
                player.getInventory().clear();
                participant.setRole(PlayerRole.INFECTEE, true, false);
                giveRoleKit(participant, PlayerRole.INFECTEE);
            } else {
                syncParticipantState(participant);
            }
            player.setPlayerListName(participant.getRole().getNameColor() + player.getName());
        }
        checkState();
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        bossBar.removePlayer(player);
        final Participant participant = getParticipant(player);
        if (participant != null && shouldInfectOnQuit(participant)) {
            pendingInfecteeKitPlayers.add(player.getUniqueId());
            participant.setRole(PlayerRole.INFECTEE, false, false);
            removeGameModifiers(player);
            StatView.get(player).addValue(Stats.CELL_METAMORPHOSIS, 1);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                checkState();
            }
        }.runTaskLater(ZombieGame.getInstance(), 5);
    }

    public void prepareRoleState(Player player) {
        removeGameModifiers(player);
        player.setGlowing(false);
    }

    private void syncParticipantState(Participant participant) {
        if (participant == null || !isRunning() || currentPhase == GamePhase.PRE_GAME) return;

        final Player player = participant.getPlayer();
        removeGameModifiers(player);
        participant.getRole().applyRuntimeState(participant, false);

        try {
            if (participant.getRole().isZombie() && currentPhase == GamePhase.NIGHT) {
                player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).addModifier(MODIFIER_SPEED);
                player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).addModifier(MODIFIER_DAMAGE);
            }
        } catch (IllegalArgumentException ignored) {}

        if (participant.getRole().isHuman() && participant.getRole() != PlayerRole.DEFAULT && lastHumanBuff && getHumanCount() == 1) {
            try {
                player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).addModifier(MODIFIER_HUMAN_SPEED);
                player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).addModifier(MODIFIER_HUMAN_DAMAGE);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public Collection<? extends Participant> getParticipants() {
        return Collections.unmodifiableCollection(participants.values());
    }

    public Collection<? extends Participant> getParticipantsOnline() {
        final List<Participant> collect = new ArrayList<>();
        for (Participant participant : participants.values()) {
            if (participant.getPlayer().isOnline()) collect.add(participant);
        }
        return Collections.unmodifiableCollection(collect);
    }

    public boolean isParticipating(Player player) {
        return participants.containsKey(player.getUniqueId());
    }

    public boolean isParticipating(UUID uuid) {
        return participants.containsKey(uuid);
    }

    public Participant getParticipant(Player player) {
        return getParticipant(player.getUniqueId());
    }

    public Participant getParticipant(UUID uuid) {
        return participants.get(uuid);
    }

    public Participant addParticipant(Player player) {
        return addParticipant(player, true);
    }

    public Participant addParticipant(Player player, boolean checkState) {
        Participant participant = participants.computeIfAbsent(player.getUniqueId(), uuid -> new Participant(player));
        if (checkState) checkState();
        return participant;
    }

    public Participant assignRole(Player player, PlayerRole role, boolean giveKit) {
        Participant participant = addParticipant(player, false);
        player.getInventory().clear();
        participant.setRole(role, true, false);
        if (giveKit) {
            giveRoleKit(participant, role);
        }
        checkState();
        return participant;
    }

    public void removeParticipant(UUID uuid) {
        final Participant removed = participants.remove(uuid);
        if (removed != null) {
            removed.setRole(PlayerRole.DEFAULT, true, false);
            checkState();
        }
    }




    @Override
    public boolean start() {
        if (GameManager.currentGame == null && super.start()) {
            GameManager.currentGame = this;
            return true;
        }
        return false;
    }

    @Override
    public boolean stop(boolean silent) {
        if (GameManager.currentGame == this && super.stop(false)) {
            GameManager.currentGame = null;
            return true;
        }
        return false;
    }

    public boolean stop() {
        if (GameManager.currentGame == this && super.stop(false)) {
            GameManager.currentGame = null;
            return true;
        }
        return false;
    }

    @Override
    protected void onEnd() {
        this.onSilentEnd();
    }

    @Override
    protected void onSilentEnd() {
        for (Participant participant : getParticipants()) {
            removeGameModifiers(participant.getPlayer());
        }
        HandlerList.unregisterAll(this);
        bossBar.removeAll();
        for (Team value : scoreboardTeams.values()) {
            value.unregister();
        }
        StatView.saveAll();
    }

    @Override
    public boolean pause() {
        return false;
    }

    @Override
    public boolean resume() {
        return false;
    }

    public class Participant implements Observer {

        private @NotNull Player player;
        private final Listener listener;
        private final Attributes attributes = new Attributes();
        private PlayerRole role;
        private BukkitRunnable runOnRespawn = null;

        protected Participant(@NotNull Player player) {
            this.player = player;
            this.role = PlayerRole.DEFAULT;
            this.listener = new Listener() {
                @EventHandler
                public void onPlayerLogin(PlayerLoginEvent e) {
                    if (e.getPlayer().getUniqueId().equals(Participant.this.player.getUniqueId())) {
                        Participant.this.player = e.getPlayer();
                    }
                }

                @EventHandler
                public void onRegainHealth(EntityRegainHealthEvent e) {
                    if (Participant.this.player.getUniqueId().equals(e.getEntity().getUniqueId())) {
                        if (role.isZombie()) return;
                        if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
                            e.setCancelled(true);
                        }
                    }
                }

                @EventHandler
                public void onPlayerRespawn(PlayerRespawnEvent e) {
                    if (Participant.this.player.getUniqueId().equals(e.getPlayer().getUniqueId())) {
                        e.setRespawnLocation(spawn);
                        if (runOnRespawn != null) {
                            runOnRespawn.runTaskLater(ZombieGame.getInstance(), 3);
                            runOnRespawn = null;
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                final int health = BaseConfig.getRoleHealth(Participant.this.getRole());
                                Participant.this.player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(health);
                                Participant.this.player.setHealth(health);
                            }
                        }.runTaskLater(ZombieGame.getInstance(), 2);
                    }
                }

                @EventHandler
                public void onPlayerDeath(PlayerDeathEvent e) {
                    if (Participant.this.player.getUniqueId().equals(e.getEntity().getUniqueId())) {
//
//                        new BukkitRunnable() {
//                            @Override
//                            public void run() {
//                                NMS.respawn(getPlayer());
//                                getPlayer().teleport(spawn);
//                            }
//                        }.runTaskLater(ZombieGame.getInstance(), 2);

                        if (role.isZombie()) {
                            final Player killer = e.getEntity().getKiller();
                            if (killer == null) return;
                            final Participant kPart = getParticipant(killer);
                            StatView.get(kPart.getPlayer()).addValue(Stats.EXTERMINATION, 1);
                            if (kPart.role == PlayerRole.HERO) {
                                StatView.get(kPart.getPlayer()).addValue(Stats.SUPER_SOLDIER, 1);
                            }


                            MOST_KILL_ZOMBIE.addKills(kPart);
                            zombieKillCounts.put(kPart.getPlayer().getUniqueId(), zombieKillCounts.getOrDefault(kPart.getPlayer().getUniqueId(), 0) + 1);


                            if (!firstKill) {
                                firstKill = true;
                                Bukkit.broadcastMessage(Texts.text(TextNodes.GAME_FIRST_KILL)
                                        .replaceAll("%s", getPlayer().getName())
                                        .replaceAll("%k", kPart.getPlayer().getName()));
                                StatView.get(kPart.getPlayer()).addValue(Stats.FIRST_KILL, 1);
                            }
                            if (role == PlayerRole.INITIAL_ZOMBIE) {
                                StatView.get(kPart.getPlayer()).addValue(Stats.HOST_REMOVAL, 1);
                            }

                            // 손에 뭔가 들고 있는 경우에만, 소드마스터 통계값 올라감
                            if (getPlayer().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                                final ItemStack mainHand = kPart.getPlayer().getInventory().getItemInMainHand();
                                if (mainHand == null || mainHand.getType() == Material.AIR) return;
                                StatView.get(kPart.getPlayer()).addValue(Stats.SWORD_MASTER, 1);
                            }

                            if (BaseConfig.getBoolean(BaseNodes.ZOMBIE_INVENTORY_CLEAR)) {
                                runOnRespawn = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        getPlayer().getInventory().clear();
                                        getPlayer().getInventory().addItem(Kits.getKit(KitNodes.INFECTEE).toArray(new ItemStack[0]));
                                    }
                                };
                            } else {
                                e.setKeepInventory(true);
                            }
                        } else {
                            final int survivorCount = getHumanCount();
                            if (survivorCount >= 1) {
                                if (role == PlayerRole.HERO) {
                                    final Player killer = getPlayer().getKiller();
                                    if (killer != null) {
                                        final Participant kPart = getParticipant(killer);
                                        StatView.get(kPart.getPlayer()).addValue(Stats.VILLAIN, 1);
                                    }
                                }


                                final Player killer = getPlayer().getKiller();
                                if (killer != null) {
                                    final Participant kPart = getParticipant(killer);


                                    MOST_KILL_HUMAN.addKills(kPart);
                                    humanKillCounts.put(kPart.getPlayer().getUniqueId(), humanKillCounts.getOrDefault(kPart.getPlayer().getUniqueId(), 0) + 1);


                                    StatView.get(kPart.getPlayer()).addValue(Stats.BREEDING, 1);
                                    if (kPart.getRole() == PlayerRole.INITIAL_ZOMBIE) {
                                        StatView.get(kPart.getPlayer()).addValue(Stats.OUTSTANDING_GENE, 1);
                                    }
                                    if (!firstDeath) {
                                        firstDeath = true;
                                        Bukkit.broadcastMessage(Texts.text(TextNodes.GAME_FIRST_DEATH)
                                                .replaceAll("%s", getPlayer().getName())
                                                .replaceAll("%k", kPart.getPlayer().getName()));
                                        StatView.get(kPart.getPlayer()).addValue(Stats.FIRST_DEATH, 1);
                                    }
                                }

                                if (role == PlayerRole.HERO && killer != null) {
                                    Bukkit.broadcastMessage(Texts.text(TextNodes.GAME_HERO_INFECTED)
                                            .replaceAll("%s", getPlayer().getName())
                                            .replaceAll("%k", killer.getName()));
                                }

                                runOnRespawn = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        setRole(PlayerRole.INFECTEE, true, false);
                                        giveRoleKit(Participant.this, PlayerRole.INFECTEE);
                                        checkState();
                                    }
                                };
                                StatView.get(getPlayer()).addValue(Stats.CELL_METAMORPHOSIS, 1);
                            }
                        }
                        checkState();
                    }
                }

                @EventHandler
                public void onVaccine(EntityDamageByEntityEvent e) {
                    if (Participant.this.player.getUniqueId().equals(e.getDamager().getUniqueId())) {
                        final ItemStack mainHand = getPlayer().getInventory().getItemInMainHand();
                        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && mainHand.hasItemMeta() && Texts.text(TextNodes.ITEM_VACCINE_NAME).equals(mainHand.getItemMeta().getDisplayName())) {
                            if (e.getEntity() instanceof Player) {
                                final Participant target = getParticipant(e.getEntity().getUniqueId());
                                if (target != null && target.role == PlayerRole.INFECTEE && getPlayer().getLocation().distanceSquared(target.getPlayer().getLocation()) <= 14) {
                                    mainHand.setAmount(mainHand.getAmount() - 1);
                                    getPlayer().getInventory().setItemInMainHand(mainHand);
                                    target.getPlayer().getInventory().clear();
                                    target.setRole(PlayerRole.SURVIVOR, true, false);
                                    giveRoleKit(target, PlayerRole.SURVIVOR);
                                    checkState();

                                    if (getHumanCount() >= 2) {
                                        lastHumanBuff = false;
                                        for (Participant human : getHumans()) {
                                            human.getPlayer().getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).removeModifier(MODIFIER_HUMAN_SPEED);
                                        }
                                    }



                                    getPlayer().sendMessage(Texts.text(TextNodes.GAME_VACCINE_USE_MESSAGE));
                                    Bukkit.broadcastMessage(
                                            Texts.text(TextNodes.GAME_VACCINE_USE_BROADCAST)
                                                    .replaceAll("%t", target.getPlayer().getName())
                                                    .replaceAll("%p", getPlayer().getName())
                                    );
                                    StatView.get(getPlayer()).addValue(Stats.ROMANTIC_DOCTOR, 1);


                                    target.getPlayer().sendMessage(Texts.text(TextNodes.GAME_VACCINE_CURED));
                                }
                            }
                        }
                    }
                }

                @EventHandler(priority = EventPriority.HIGHEST)
                public void onAttack(EntityDamageByEntityEvent e) {
                    final Entity damager = getDamager(e.getDamager());
                    if (damager != null && Participant.this.player.getUniqueId().equals(damager.getUniqueId())) {
                        if (e.getEntity() instanceof Player) {
                            final Participant target = getParticipant(e.getEntity().getUniqueId());
                            if (target != null) {
                                if ((target.getRole().isHuman() && getRole().isHuman()) || (target.getRole().isZombie() && getRole().isZombie())) {
                                    e.setCancelled(true);
                                    getPlayer().sendMessage(Texts.text(TextNodes.GAME_NO_TEAMKILL));
                                }




                                if (target.role.isZombie()) {
                                    StatView.get(getPlayer()).addValue(Stats.SADISM, (int) e.getFinalDamage());
                                    StatView.get(target.getPlayer()).addValue(Stats.MASOCHISM, (int) e.getFinalDamage());
                                }
                            }
                        }
                    }
                }

                @Nullable
                private Entity getDamager(final Entity damager) {
                    if (damager instanceof Projectile) {
                        final ProjectileSource shooter = ((Projectile) damager).getShooter();
                        return shooter instanceof Entity ? (Entity) shooter : null;
                    } else return damager;
                }

            };
            Game.this.attachObserver(this);
            Bukkit.getPluginManager().registerEvents(listener, ZombieGame.getInstance());
        }

        @Override
        public void run(int count) {
            if (role.isZombie()) {
                player.addPotionEffect(Game.NIGHT_VISION, true);
                if (currentPhase == GamePhase.NIGHT) {
                    try {
                        player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).addModifier(MODIFIER_SPEED);
                        player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).addModifier(MODIFIER_DAMAGE);
                    } catch (IllegalArgumentException ignored) {}
                } else {
                    player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).removeModifier(MODIFIER_SPEED);
                    player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).removeModifier(MODIFIER_DAMAGE);
                }
            }
        }

        @Override
        public void onEnd() {
            HandlerList.unregisterAll(listener);
            for (Participant participant : participants.values()) {
                removeGameModifiers(participant.getPlayer());
                PlayerRole.DEFAULT.setup(participant);
            }
        }

        public PlayerRole getRole() {
            return role;
        }

        public PlayerRole setRole(@NotNull PlayerRole role) {
            return setRole(role, true, true);
        }

        private PlayerRole setRole(@NotNull PlayerRole role, boolean applySetup, boolean checkState) {
            this.role = role;
            if (isRunning() && applySetup) {
                role.setup(this);
            }
            if (checkState) {
                checkState();
            }
            return role;
        }

        @NotNull
        public Player getPlayer() {
            return player;
        }

        @NotNull
        public Game getGame() {
            return Game.this;
        }

        public Attributes attributes() {
            return attributes;
        }

        public class Attributes {
        }

        public class Attribute<T> {

            private final T defaultValue;
            private T value;

            public Attribute(T defaultValue) {
                this.defaultValue = defaultValue;
                this.value = defaultValue;
            }

            public T getDefaultValue() {
                return defaultValue;
            }

            public T getValue() {
                return value;
            }

            public T setValue(T value) {
                T origin = this.value;
                this.value = value;
                return origin;
            }

        }

        public class SetAttribute<E> {

            private final Set<E> set;

            @SafeVarargs
            public SetAttribute(E... defaultElements) {
                this.set = new HashSet<>();
                Collections.addAll(set, defaultElements);
            }

            public Set<E> getView() {
                return Collections.unmodifiableSet(set);
            }

            public void addValue(E element) {
                set.add(element);
            }

            public void removeValue(E element) {
                set.remove(element);
            }

        }

    }

    private void snapshotApiState() {
        Map<UUID, PlayerRole> roles = new HashMap<>();
        for (Participant participant : getParticipants()) {
            roles.put(participant.getPlayer().getUniqueId(), participant.getRole());
        }
        GameManager.updateSnapshot(getHumanCount(), getZombieCount(), roles, humanKillCounts, zombieKillCounts, getTotalGameTime(), getTotalElapsedTime(), getTotalRemainingTime());
    }

    private void resetParticipantAfterGame(Participant participant) {
        Player player = participant.getPlayer();
        if (player.isOnline()) {
            resetPlayerAfterGame(player);
        } else {
            pendingResetPlayers.add(player.getUniqueId());
            GameManager.addPendingReset(player.getUniqueId());
        }
    }

    public static void resetPlayerAfterGame(Player player) {
        removeGameModifiers(player);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20);
        player.setHealth(Math.min(20, player.getAttribute(Attribute.MAX_HEALTH).getValue()));
        player.setPlayerListName(player.getName());
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.1F);
        player.setGlowing(false);
    }

    public static void removeGameModifiers(Player player) {
        removeModifier(player, Attribute.MOVEMENT_SPEED, ZOMBIE_SPEED_KEY);
        removeModifier(player, Attribute.ATTACK_DAMAGE, ZOMBIE_DAMAGE_KEY);
        removeModifier(player, Attribute.MOVEMENT_SPEED, HUMAN_SPEED_KEY);
        removeModifier(player, Attribute.ATTACK_DAMAGE, HUMAN_DAMAGE_KEY);
    }

    private static void removeModifier(Player player, Attribute attribute, NamespacedKey key) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;
        for (AttributeModifier modifier : new ArrayList<>(instance.getModifiers())) {
            if (modifier.getKey().equals(key)) {
                instance.removeModifier(modifier);
            }
        }
    }

    private boolean shouldInfectOnQuit(Participant participant) {
        return isRunning()
                && currentPhase != GamePhase.PRE_GAME
                && (participant.getRole() == PlayerRole.SURVIVOR || participant.getRole() == PlayerRole.HERO);
    }

    private void giveRoleKit(Participant participant, PlayerRole role) {
        KitNodes node = getKitNode(role);
        if (node == null) return;
        participant.getPlayer().getInventory().addItem(Kits.getKit(node).toArray(new ItemStack[0]));
    }

    private KitNodes getKitNode(PlayerRole role) {
        switch (role) {
            case SURVIVOR:
                return KitNodes.SURVIVOR;
            case HERO:
                return KitNodes.HERO;
            case INITIAL_ZOMBIE:
                return KitNodes.INITIAL_ZOMBIE;
            case INFECTEE:
                return KitNodes.INFECTEE;
            default:
                return null;
        }
    }

    public void checkState() {
        if (suppressStateChecks) return;
        if (currentPhase == GamePhase.PRE_GAME) return;
        if (!isRunning()) return;
        final int survivorCount = getHumanCount();
        if (survivorCount >= 1) {
            if (!vaccineDistributed && survivorCount <= (initialSurvivors / 2)) {
                vaccineDistributed = true;
                final int vaccineCount = BaseConfig.getInt(BaseNodes.AMOUNT_VACCINE);
                final List<Participant> humans = getHumans();
                final ItemStack vaccine = new ItemBuilder(MaterialX.EMERALD)
                        .displayName(Texts.text(TextNodes.ITEM_VACCINE_NAME))
                        .lore(Texts.text(TextNodes.ITEM_VACCINE_LORE).split("\n"))
                        .build();
                for (int i = 0; i < vaccineCount; i++) {
                    final Player target = random.pick(humans).player;
                    target.getInventory().addItem(vaccine);
                    target.sendMessage(Texts.text(TextNodes.GAME_VACCINE_GOT));
                }
                Bukkit.broadcastMessage(Texts.text(TextNodes.GAME_VACCINE_NOTICE));
            }
            if (survivorCount == 1) {
                if (!lastHumanBuff) {
                    for (Participant participant : getParticipantsOnline()) {
                        if (participant.role.isHuman() && participant.role != PlayerRole.DEFAULT) {
                            lastHumanBuff = true;
                            try {
                                participant.getPlayer().getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).addModifier(MODIFIER_HUMAN_SPEED);
                                participant.getPlayer().getAttribute(Attribute.ATTACK_DAMAGE).addModifier(MODIFIER_HUMAN_DAMAGE);
                            } catch (IllegalArgumentException ignored) {}
                            final Player lastPlayer = participant.getPlayer();
                            lastPlayer.sendMessage(Texts.text(TextNodes.GAME_LAST_SURVIVOR_MESSAGE));
                            lastPlayer.sendTitle(Texts.text(TextNodes.GAME_LAST_SURVIVOR_MESSAGE), "", 10, 70, 20);
                            lastPlayer.playSound(lastPlayer.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
                            CommandConfig.dispatch(CommandNodes.LAST_SURVIVOR, lastPlayer);
                            Bukkit.broadcastMessage(Texts.text(TextNodes.GAME_BUFF_LAST_HUMAN_MESSAGE));
                            Bukkit.broadcastMessage(String.format(Texts.text(TextNodes.GAME_BUFF_LAST_HUMAN_BROADCAST), participant.getPlayer().getName()));
                            break;
                        }
                    }
                }
            } else if (lastHumanBuff) {
                for (Participant participant : getParticipantsOnline()) {
                    if (participant.role.isHuman() && participant.role != PlayerRole.DEFAULT) {
                        try {
                            participant.getPlayer().getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).removeModifier(MODIFIER_HUMAN_SPEED);
                            participant.getPlayer().getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).removeModifier(MODIFIER_HUMAN_DAMAGE);
                        } catch (IllegalArgumentException ignored) {}
                        break;
                    }
                }
                lastHumanBuff = false;
            }
        } else if (survivorCount == 0) {
            for (Participant participant : getParticipantsOnline()) {
                if (participant.role.isZombie()) {
                    StatView.get(participant.getPlayer()).addValue(Stats.NO_ONE_LEFT, 1);
                    if (participant.role == PlayerRole.INITIAL_ZOMBIE) {
                        StatView.get(participant.getPlayer()).addValue(Stats.APOCALYPSE, 1);
                    }
                }
            }
            Bukkit.broadcastMessage(Texts.text(TextNodes.GAME_RESULT_ZOMBIES_WIN));
            CommandConfig.dispatch(CommandNodes.GAME_END_ZOMBIES_WIN);
            MOST_KILL_ZOMBIE.print();
            MOST_KILL_HUMAN.print();
            endGame();
        }
    }

}
