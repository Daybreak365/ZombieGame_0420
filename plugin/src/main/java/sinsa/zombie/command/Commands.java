package sinsa.zombie.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sinsa.zombie.Messages;
import sinsa.zombie.ZombieGame;
import sinsa.zombie.config.base.BaseConfig;
import sinsa.zombie.config.base.serializable.SpawnLocation;
import sinsa.zombie.config.base.wizard.BaseConfigWizard;
import sinsa.zombie.config.command.CommandConfig;
import sinsa.zombie.config.kit.KitNodes;
import sinsa.zombie.config.kit.Kits;
import sinsa.zombie.config.kit.wizard.KitWizard;
import sinsa.zombie.config.text.TextNodes;
import sinsa.zombie.config.text.Texts;
import sinsa.zombie.game.Game;
import sinsa.zombie.game.GameManager;
import sinsa.zombie.game.PlayerRole;
import sinsa.zombie.stat.StatGUI;
import sinsa.zombie.utils.Numbers;
import sinsa.zombie.utils.language.Formatter;
import sinsa.zombie.utils.language.korean.KoreanUtil;
import sinsa.zombie.utils.logging.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Commands implements CommandExecutor, TabCompleter {

	private static final Logger logger = Logger.getLogger(Commands.class);
	private static final ChatColor BRACKET_COLOR = ChatColor.DARK_GREEN, TEXT_COLOR = ChatColor.GREEN;

	private final ZombieGame plugin;
	private final Command mainCommand;

	public Command getMainCommand() {
		return mainCommand;
	}

	public Commands(ZombieGame plugin) {
		this.plugin = plugin;
		this.mainCommand = new Command() {
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				if (args.length == 0) {
					sender.sendMessage(Formatter.formatTitle(BRACKET_COLOR, TEXT_COLOR, Texts.text(TextNodes.PLUGIN_NAME)));
					sender.sendMessage("§c버전 §7: §f" + plugin.getDescription().getVersion());
					sender.sendMessage("§3§o/" + command + " help §7§o로 명령어 도움말을 확인하세요.");
					return true;
				}
				return false;
			}
		};
		mainCommand.addSubCommand("help", new Command() {
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				if (args.length > 0) {
					if (Numbers.isInt(args[0])) {
						sendHelp(sender, command, Integer.parseInt(args[0]));
					} else {
						Messages.sendError(sender, "존재하지 않는 페이지입니다.");
					}
				} else {
					sendHelp(sender, command, 1);
				}
				return true;
			}

			private void sendHelp(CommandSender sender, String command, int page) {
				final int allPage = 1;
				switch (page) {
					case 1:
						sender.sendMessage(new String[]{
								Formatter.formatTitle(BRACKET_COLOR, TEXT_COLOR, Texts.text(TextNodes.PLUGIN_NAME)),
								"§b/" + command + " help <페이지> §7로 더 많은 명령어를 확인하세요! ( §b" + page + " 페이지 §7/ §b" + allPage + " 페이지 §7)",
								Formatter.formatCommand(command, "start", "게임을 시작시킨다.", true),
								Formatter.formatCommand(command, "stop", "게임을 종료시킨다.", true),
								Formatter.formatCommand(command, "spawn", "스폰 설정 명령어 목록을 확인한다.", true),
								Formatter.formatCommand(command, "config", "게임 설정 GUI를 연다.", true),
								Formatter.formatCommand(command, "set", "인게임 설정", true),
								Formatter.formatCommand(command, "kit", "킷 설정", true),
								Formatter.formatCommand(command, "reload", "config/text/command/kit 설정을 다시 불러온다.", true),
								Formatter.formatCommand(command, "me", "업적 확인 GUI를 연다.", false)
						});
						break;
					default:
						Messages.sendError(sender, "존재하지 않는 페이지입니다.");
						break;
				}
			}

		});
		mainCommand.addSubCommand("start", new Command(Command.Condition.OP) {
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				if (!GameManager.isRunning()) {
					if (GameManager.start()) {
						Bukkit.broadcastMessage(String.format(Texts.text(TextNodes.COMMAND_GAME_START), sender.getName()));
					}
				} else {
					Messages.sendError(sender, "게임이 이미 진행되고 있습니다.");
				}
				return true;
			}
		});
		mainCommand.addSubCommand("stop", new Command(Command.Condition.OP) {
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				if (GameManager.isRunning()) {
					if (GameManager.stop()) {
						Bukkit.broadcastMessage(String.format(Texts.text(TextNodes.COMMAND_GAME_STOP), sender.getName()));
					}
				} else {
					Messages.sendError(sender, "게임이 진행되고 있지 않습니다.");
				}
				return true;
			}
		});
		mainCommand.addSubCommand("spawn", new Command(Command.Condition.OP) {
			{
				addSubCommand("list", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						sender.sendMessage(Formatter.formatTitle(BRACKET_COLOR, TEXT_COLOR, "스폰 목록"));
						final SpawnLocation nextSpawn = BaseConfig.SpawnLocations.getNextSpawn();
						for (Map.Entry<String, SpawnLocation> entry : BaseConfig.SpawnLocations.getEntries()) {
							final SpawnLocation location = entry.getValue();
							if (location == nextSpawn) {
								sender.sendMessage("§8▶ §c" + entry.getKey() + "§8, §6world§e: §f" + location.world + "§8, §6x§e: §f" + location.x + "§8, §6y§e: §f" + location.y + "§8, §6z§e: §f" + location.z + " §2§l[선택됨]§r");
							} else {
								sender.sendMessage("§8▶ §c" + entry.getKey() + "§8, §6world§e: §f" + location.world + "§8, §6x§e: §f" + location.x + "§8, §6y§e: §f" + location.y + "§8, §6z§e: §f" + location.z);
							}
						}
						return true;
					}
				});
				addSubCommand("add", new Command(Condition.PLAYER) {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						if (args.length != 0) {
							if (BaseConfig.SpawnLocations.isAbsent(args[0])) {
								if (!"random".equalsIgnoreCase(args[0]) && !"랜덤".equalsIgnoreCase(args[0])) {
									final Player player = (Player) sender;
									final SpawnLocation location = SpawnLocation.fromVanilla(player.getLocation());
									BaseConfig.SpawnLocations.add(args[0], location);
									sender.sendMessage("§2§l스폰 추가됨 §r§8▶ §c" + args[0] + "§8, §6world§e: §f" + location.world + "§8, §6x§e: §f" + location.x + "§8, §6y§e: §f" + location.y + "§8, §6z§e: §f" + location.z);
								} else Messages.sendError(sender, "'§4" + args[0] + "§c'은 사용할 수 없는 이름입니다.");
							} else Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 이미 존재하는 스폰 이름입니다.");
						} else Messages.sendError(sender, "<이름>을 입력해야 합니다.");
						return true;
					}
				});
				addSubCommand("remove", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						if (args.length != 0) {
							if (BaseConfig.SpawnLocations.contains(args[0])) {
								BaseConfig.SpawnLocations.remove(args[0]);
								sender.sendMessage("§4§l스폰 제거됨 §r§8▶ §c" + args[0]);
							} else Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 스폰 이름입니다.");
						} else Messages.sendError(sender, "<이름>을 입력해야 합니다.");
						return true;
					}
				});
				addSubCommand("set", new Command(Condition.PLAYER) {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						if (args.length != 0) {
							if ("random".equalsIgnoreCase(args[0]) || "랜덤".equalsIgnoreCase(args[0])) {
								BaseConfig.SpawnLocations.setNextSpawn("random");
								sender.sendMessage("§2§l다음 게임 스폰 위치가 §e§l랜덤§2§l으로 설정됐습니다.");
							} else {
								if (BaseConfig.SpawnLocations.contains(args[0])) {
									BaseConfig.SpawnLocations.setNextSpawn(args[0]);
									sender.sendMessage("§2§l다음 게임 스폰 위치가 §e§l" + args[0] + "§2§l" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.으로로) + " 설정됐습니다.");
								} else Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 스폰 이름입니다.");
							}
						} else Messages.sendError(sender, "<이름>을 입력해야 합니다.");
						return true;
					}
				});
			}
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				Player player = (Player) sender;
				if (args.length > 0) {
					Messages.sendError(player, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 콘피그입니다.");
				} else sendHelp(player, command);
				return true;
			}

			private void sendHelp(CommandSender sender, String label) {
				sender.sendMessage(new String[]{
						Formatter.formatTitle(BRACKET_COLOR, TEXT_COLOR, "스폰 설정"),
						Formatter.formatCommand(label + " spawn", "list", "스폰 장소 목록을 출력한다.", true),
						Formatter.formatCommand(label + " spawn", "set <이름>", "다음 게임 스폰 장소를 <이름>으로 설정한다.", true),
						Formatter.formatCommand(label + " spawn", "set random", "스폰 장소를 랜덤으로 설정한다.", true),
						Formatter.formatCommand(label + " spawn", "add <이름>", "현 위치를 스폰 장소로 추가한다.", true),
						Formatter.formatCommand(label + " spawn", "remove <이름>", "스폰 장소를 제거한다.", true)
				});
			}
		});
		mainCommand.addSubCommand("set", new Command(Command.Condition.OP) {
			{
				addSubCommand("0", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						if (!GameManager.isRunning()) {
							Messages.sendError(sender, "게임이 진행중이지 않습니다.");
							return true;
						}
						final Game game = GameManager.getCurrentGame();
						if (args.length == 0) {
							Messages.sendError(sender, "<닉네임>을 입력해야 합니다.");
							return true;
						}
						Game.Participant found = null;
						for (Game.Participant search : game.getParticipants()) {
							if (search.getPlayer().getName().equalsIgnoreCase(args[0])) {
								found = search;
								break;
							}
						}
						if (found == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 게임에 참여하고 있지 않습니다.");
							return true;
						}
						game.removeParticipant(found.getPlayer().getUniqueId());
						sender.sendMessage("§4§l" + found.getPlayer().getName() + "§c§l" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.을를) + " 게임에서 제외했습니다.");
						return true;
					}
				});
				addSubCommand("1", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						if (!GameManager.isRunning()) {
							Messages.sendError(sender, "게임이 진행중이지 않습니다.");
							return true;
						}
						final Game game = GameManager.getCurrentGame();
						if (args.length == 0) {
							Messages.sendError(sender, "<닉네임>을 입력해야 합니다.");
							return true;
						}
						final Player target = Bukkit.getPlayerExact(args[0]);
						if (target == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 플레이어입니다.");
							return true;
						}
						final Game.Participant participant = game.getParticipant(target.getUniqueId());
						if (participant == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 게임에 참가하고 있지 않습니다.");
							return true;
						}
						if (participant.getRole() == PlayerRole.SURVIVOR) {
							Messages.sendError(sender, "대상이 이미 생존자입니다.");
							return true;
						}
						participant.setRole(PlayerRole.SURVIVOR);
						sender.sendMessage("§2§l" + args[0] + "§a§l" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.을를) + " 생존자로 변경했습니다.");
						return true;
					}
				});
				addSubCommand("2", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						if (!GameManager.isRunning()) {
							Messages.sendError(sender, "게임이 진행중이지 않습니다.");
							return true;
						}
						final Game game = GameManager.getCurrentGame();
						if (args.length == 0) {
							Messages.sendError(sender, "<닉네임>을 입력해야 합니다.");
							return true;
						}
						final Player target = Bukkit.getPlayerExact(args[0]);
						if (target == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 플레이어입니다.");
							return true;
						}
						final Game.Participant participant = game.getParticipant(target.getUniqueId());
						if (participant == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 게임에 참가하고 있지 않습니다.");
							return true;
						}
						if (participant.getRole() == PlayerRole.INFECTEE) {
							Messages.sendError(sender, "대상이 이미 감염자입니다.");
							return true;
						}
						participant.setRole(PlayerRole.INFECTEE);
						sender.sendMessage("§4§l" + args[0] + "§c§l" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.을를) + " 감염자로 변경했습니다.");
						return true;
					}
				});
				addSubCommand("3", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						if (!GameManager.isRunning()) {
							Messages.sendError(sender, "게임이 진행중이지 않습니다.");
							return true;
						}
						final Game game = GameManager.getCurrentGame();
						if (args.length == 0) {
							Messages.sendError(sender, "<닉네임>을 입력해야 합니다.");
							return true;
						}
						final Player target = Bukkit.getPlayerExact(args[0]);
						if (target == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 플레이어입니다.");
							return true;
						}
						final Game.Participant participant = game.getParticipant(target.getUniqueId());
						if (participant == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 게임에 참가하고 있지 않습니다.");
							return true;
						}
						if (participant.getRole() == PlayerRole.INITIAL_ZOMBIE) {
							Messages.sendError(sender, "대상이 이미 최초 좀비입니다.");
							return true;
						}
						participant.setRole(PlayerRole.INITIAL_ZOMBIE);
						sender.sendMessage("§4§l" + args[0] + "§c§l" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.을를) + " 최초 좀비로 변경했습니다.");
						return true;
					}
				});
				addSubCommand("4", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						if (!GameManager.isRunning()) {
							Messages.sendError(sender, "게임이 진행중이지 않습니다.");
							return true;
						}
						final Game game = GameManager.getCurrentGame();
						if (args.length == 0) {
							Messages.sendError(sender, "<닉네임>을 입력해야 합니다.");
							return true;
						}
						final Player target = Bukkit.getPlayerExact(args[0]);
						if (target == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 플레이어입니다.");
							return true;
						}
						final Game.Participant participant = game.getParticipant(target.getUniqueId());
						if (participant == null) {
							Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 게임에 참가하고 있지 않습니다.");
							return true;
						}
						if (participant.getRole() == PlayerRole.HERO) {
							Messages.sendError(sender, "대상이 이미 영웅입니다.");
							return true;
						}
						participant.setRole(PlayerRole.HERO);
						sender.sendMessage("§e§l" + args[0] + "§6§l" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.을를) + " 영웅으로 변경했습니다.");
						return true;
					}
				});
			}
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				if (args.length > 0) {
					Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 인게임 설정입니다.");
				} else sendHelp(sender, command);
				return true;
			}

			private void sendHelp(CommandSender sender, String label) {
				sender.sendMessage(new String[]{
						Formatter.formatTitle(BRACKET_COLOR, TEXT_COLOR, "인게임 설정"),
						Formatter.formatCommand(label + " set", "0 <닉네임>", "<닉네임>을 게임에서 제외한다.", true),
						Formatter.formatCommand(label + " set", "1 <닉네임>", "<닉네임>을 생존자로 변경한다.", true),
						Formatter.formatCommand(label + " set", "2 <닉네임>", "<닉네임>을 감염자로 변경한다.", true),
						Formatter.formatCommand(label + " set", "3 <닉네임>", "<닉네임>을 최초 좀비로 변경한다.", true),
						Formatter.formatCommand(label + " set", "4 <닉네임>", "<닉네임>을 영웅으로 변경한다.", true)
				});
			}
		});
		mainCommand.addSubCommand("kit", new Command(Command.Condition.OP, Command.Condition.PLAYER) {
			{
				addSubCommand("생존자", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						final Player player = (Player) sender;
						new KitWizard(player, KitNodes.SURVIVOR, plugin).show();
						return true;
					}
				});
				addSubCommand("영웅", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						final Player player = (Player) sender;
						new KitWizard(player, KitNodes.HERO, plugin).show();
						return true;
					}
				});
				addSubCommand("최초좀비", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						final Player player = (Player) sender;
						new KitWizard(player, KitNodes.INITIAL_ZOMBIE, plugin).show();
						return true;
					}
				});
				addSubCommand("감염자", new Command() {
					@Override
					protected boolean onCommand(CommandSender sender, String command, String[] args) {
						final Player player = (Player) sender;
						new KitWizard(player, KitNodes.INFECTEE, plugin).show();
						return true;
					}
				});
			}
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				if (args.length > 0) {
					Messages.sendError(sender, "'§4" + args[0] + "§c'" + KoreanUtil.getJosa(args[0], KoreanUtil.Josa.은는) + " 존재하지 않는 킷 설정입니다.");
				} else sendHelp(sender, command);
				return true;
			}

			private void sendHelp(CommandSender sender, String label) {
				sender.sendMessage(new String[]{
						Formatter.formatTitle(BRACKET_COLOR, TEXT_COLOR, "킷 설정"),
						Formatter.formatCommand(label + " kit", "생존자", "생존자 킷 설정", true),
						Formatter.formatCommand(label + " kit", "영웅", "영웅 킷 설정", true),
						Formatter.formatCommand(label + " kit", "최초좀비", "최초좀비 킷 설정", true),
						Formatter.formatCommand(label + " kit", "감염자", "감염자 킷 설정", true)
				});
			}
		});
		mainCommand.addSubCommand("reload", new Command(Command.Condition.OP) {
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				try {
					BaseConfig.instance.update();
					Texts.instance.update();
					Kits.instance.update();
					CommandConfig.instance.update();
					sender.sendMessage(Texts.text(TextNodes.PREFIX) + " §a설정을 다시 불러왔습니다.");
				} catch (IOException | InvalidConfigurationException e) {
					logger.error("설정을 다시 불러오는 중 오류가 발생했습니다: " + e);
					Messages.sendError(sender, "설정을 다시 불러오는 중 오류가 발생했습니다.");
				}
				return true;
			}
		});
		mainCommand.addSubCommand("config", new Command(Command.Condition.OP, Command.Condition.PLAYER) {
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				final Player player = (Player) sender;
				new BaseConfigWizard(player, plugin).show();
				return true;
			}
		});
		mainCommand.addSubCommand("me", new Command(Command.Condition.PLAYER) {
			@Override
			protected boolean onCommand(CommandSender sender, String command, String[] args) {
				final Player player = (Player) sender;
				if (args.length == 0) {
					new StatGUI(player, plugin).show();
				} else {
					final Player target = Bukkit.getPlayerExact(args[0]);
					if (target != null) {
						new StatGUI(player, target, plugin).show();
					} else Messages.sendError(sender, "존재하지 않는 플레이어입니다.");
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		mainCommand.handleCommand(sender, label, args);
		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, String label, String[] args) {
		return null;
	}

}
