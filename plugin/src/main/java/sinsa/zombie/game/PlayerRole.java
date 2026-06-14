package sinsa.zombie.game;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import sinsa.zombie.ZombieGame;
import sinsa.zombie.config.base.BaseConfig;
import sinsa.zombie.config.kit.KitNodes;
import sinsa.zombie.config.kit.Kits;
import sinsa.zombie.config.text.TextNodes;
import sinsa.zombie.config.text.Texts;
import sinsa.zombie.utils.compat.MaterialX;
import sinsa.zombie.utils.minecraft.item.ItemBuilder;

import java.util.Map;

public enum PlayerRole {

    DEFAULT(true, "§f", 0) {
        @Nullable
        @Override
        public ItemStack getChestplate() {
            return null;
        }
        @Override
        public void setup(Game.Participant participant) {
            final Player player = participant.getPlayer();
            participant.getGame().prepareRoleState(player);
            this.setPlayerListName(player);
            player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(20);
            player.setHealth(20);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            DEFAULT.applyArmor(player);
            player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).removeModifier(participant.getGame().MODIFIER_SPEED);
            player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).removeModifier(participant.getGame().MODIFIER_DAMAGE);


            for (Map.Entry<PlayerRole, Team> entry : participant.getGame().scoreboardTeams.entrySet()) {
                if (entry.getKey() == this) {
                    entry.getValue().addEntry(player.getName());
                } else {
                    entry.getValue().removeEntry(player.getName());
                }
            }
        }
    },
    SURVIVOR(true, "§3", 1) {
        @Override
        public @Nullable ItemStack getChestplate() {
            return new ItemBuilder(MaterialX.IRON_CHESTPLATE)
                    .displayName(Texts.text(TextNodes.ITEM_CHESTPLATE_SURVIVOR_NAME))
                    .lore(Texts.text(TextNodes.ITEM_CHESTPLATE_SURVIVOR_LORE).split("\n"))
                    .unbreakable(true)
                    .build();
        }

        @Override
        public void setup(Game.Participant participant) {
            final Player player = participant.getPlayer();
            participant.getGame().prepareRoleState(player);
            this.setPlayerListName(player);
            player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(BaseConfig.getRoleHealth(this));
            player.setHealth(BaseConfig.getRoleHealth(this));
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            SURVIVOR.applyArmor(player);
            player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).removeModifier(participant.getGame().MODIFIER_SPEED);
            player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).removeModifier(participant.getGame().MODIFIER_DAMAGE);


            for (Map.Entry<PlayerRole, Team> entry : participant.getGame().scoreboardTeams.entrySet()) {
                if (entry.getKey() == this) {
                    entry.getValue().addEntry(player.getName());
                } else {
                    entry.getValue().removeEntry(player.getName());
                }
            }
        }
    },
    HERO(true, "§1§l", 4) {
        @Override
        public @Nullable ItemStack getChestplate() {
            return new ItemBuilder(MaterialX.DIAMOND_CHESTPLATE)
                    .displayName(Texts.text(TextNodes.ITEM_CHESTPLATE_HERO_NAME))
                    .lore(Texts.text(TextNodes.ITEM_CHESTPLATE_HERO_LORE).split("\n"))
                    .unbreakable(true)
                    .build();
        }

        @Override
        public void setup(Game.Participant participant) {
            final Player player = participant.getPlayer();
            participant.getGame().prepareRoleState(player);
            this.setPlayerListName(player);
            player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(BaseConfig.getRoleHealth(this));
            player.setHealth(BaseConfig.getRoleHealth(this));
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            HERO.applyArmor(player);
            player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED).removeModifier(participant.getGame().MODIFIER_SPEED);
            player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).removeModifier(participant.getGame().MODIFIER_DAMAGE);


            for (Map.Entry<PlayerRole, Team> entry : participant.getGame().scoreboardTeams.entrySet()) {
                if (entry.getKey() == this) {
                    entry.getValue().addEntry(player.getName());
                } else {
                    entry.getValue().removeEntry(player.getName());
                }
            }
        }
    },
    INITIAL_ZOMBIE(false, "§4§l", 3) {
        @Override
        public @Nullable ItemStack getChestplate() {
            return new ItemBuilder(MaterialX.LEATHER_CHESTPLATE)
                    .displayName(Texts.text(TextNodes.ITEM_CHESTPLATE_INITIAL_ZOMBIE_NAME))
                    .lore(Texts.text(TextNodes.ITEM_CHESTPLATE_INITIAL_ZOMBIE_LORE).split("\n"))
                    .unbreakable(true)
                    .build();
        }

        @Override
        public void setup(Game.Participant participant) {
            final Player player = participant.getPlayer();
            participant.getGame().prepareRoleState(player);
            this.setPlayerListName(player);
            player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(BaseConfig.getRoleHealth(this));
            player.setHealth(BaseConfig.getRoleHealth(this));
            player.addPotionEffect(Game.NIGHT_VISION, true);
            player.getInventory().clear();
            INITIAL_ZOMBIE.applyArmor(player);


            for (Map.Entry<PlayerRole, Team> entry : participant.getGame().scoreboardTeams.entrySet()) {
                if (entry.getKey() == this) {
                    entry.getValue().addEntry(player.getName());
                } else {
                    entry.getValue().removeEntry(player.getName());
                }
            }
        }
    },
    INFECTEE(false, "§c", 2) {
        @Override
        public @Nullable ItemStack getChestplate() {
            return null;
        }

        @Override
        public void setup(Game.Participant participant) {
            final Player player = participant.getPlayer();
            participant.getGame().prepareRoleState(player);
            this.setPlayerListName(player);
            player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(BaseConfig.getRoleHealth(this));
            player.setHealth(BaseConfig.getRoleHealth(this));
            player.addPotionEffect(Game.NIGHT_VISION, true);
            player.getInventory().clear();
            INFECTEE.applyArmor(player);


            for (Map.Entry<PlayerRole, Team> entry : participant.getGame().scoreboardTeams.entrySet()) {
                if (entry.getKey() == this) {
                    entry.getValue().addEntry(player.getName());
                } else {
                    entry.getValue().removeEntry(player.getName());
                }
            }
        }
    };

    private final boolean human;
    private final String nameColor;
    private final int apiCode;

    PlayerRole(boolean human, String nameColor) {
        this(human, nameColor, -1);
    }

    PlayerRole(boolean human, String nameColor, int apiCode) {
        this.human = human;
        this.nameColor = nameColor;
        this.apiCode = apiCode;
    }

    public boolean isHuman() {
        return human;
    }

    public boolean isZombie() {
        return !human;
    }

    @Nullable
    public abstract ItemStack getChestplate();

    public abstract void setup(Game.Participant participant);

    public String getNameColor() {
        return nameColor;
    }
    public int getApiCode() {
        return apiCode;
    }


    void applyRuntimeState(Game.Participant participant, boolean clearInventory) {
        final Player player = participant.getPlayer();
        participant.getGame().prepareRoleState(player);
        this.setPlayerListName(player);
        if (this == DEFAULT) {
            player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(20);
            player.setHealth(20);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else {
            player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(BaseConfig.getRoleHealth(this));
            player.setHealth(BaseConfig.getRoleHealth(this));
            if (isZombie()) {
                player.addPotionEffect(Game.NIGHT_VISION, true);
            } else {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
        }
        if (clearInventory && isZombie()) {
            player.getInventory().clear();
        }
        applyArmor(player);
        updateScoreboardTeam(participant, player);
    }

    private void applyArmor(Player player) {
        player.getInventory().setArmorContents(getArmorContents());
    }

    private ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[4];
        KitNodes node = getKitNode();
        if (node != null) {
            int index = 0;
            for (ItemStack item : Kits.getArmorKit(node)) {
                if (index >= armor.length) break;
                armor[index++] = item;
            }
        }
        if (armor[2] == null) {
            armor[2] = getChestplate();
        }
        return armor;
    }

    private KitNodes getKitNode() {
        switch (this) {
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

    private void updateScoreboardTeam(Game.Participant participant, Player player) {
        for (Map.Entry<PlayerRole, Team> entry : participant.getGame().scoreboardTeams.entrySet()) {
            if (entry.getKey() == this) {
                entry.getValue().addEntry(player.getName());
            } else {
                entry.getValue().removeEntry(player.getName());
            }
        }
    }

    void setPlayerListName(Player player) {
        player.setPlayerListName(getNameColor() + player.getName());
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                count++;
                player.setPlayerListName(getNameColor() + player.getName());
                if (count >= 5) cancel();
            }
        }.runTaskTimer(ZombieGame.getInstance(), 0, 1);
    }
}
