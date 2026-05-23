package sinsa.zombie.config.base;

import sinsa.zombie.config.cached.Cacher;
import sinsa.zombie.config.cached.Node;

public enum BaseNodes implements Node {

	// ○ [선정 시간] 게임 시작 전, 역할이 결정되는 시간. 기본 값 : 60
	PHASE_LENGTH_PRE_GAME(
			"phase.length.pregame",
			60
	),
	// ○ [게임 시간] 게임이 진행되는 시간. 기본 값 : 240
	PHASE_LENGTH_DAYTIME(
			"phase.length.daytime",
			240
	),
	// ○ [밤 지날 시간] 밤이 다가올(?) 시간. 기본 값 : 150
	PHASE_LENGTH_SUNSET(
			"phase.length.sunset",
			150
	),
	// ○ [밤 지속 시간] 밤이 지속되는 시간. 기본 값 : 60
	PHASE_LENGTH_NIGHT(
			"phase.length.night",
			60
	),
	PHASE_LENGTH_END(
			"phase.length.end",
			30
	),
	//	○ [최초좀비 수] 최초 좀비로 선정되는 사람의 수. 기본 값 : 2
	AMOUNT_INITIAL_ZOMBIE(
			"amount.initial_zombie",
			2
	),
	//	○ [영웅 수] 영웅으로 선정되는 사람의 수. 기본 값 : 1
	AMOUNT_HERO(
			"amount.hero",
			1
	),
	//	○ [백신 수] 백신이 지급될 사람의 수. 기본 값 : 1
	AMOUNT_VACCINE(
			"amount.vaccine",
			1
	),
	//	○ [생존자 체력] 생존자의 체력을 설정합니다. 기본 값 : 40
	HEALTH_SURVIVOR(
			"health.survivor",
			40
	),
	//	○ [영웅 체력] 영웅의 체력을 설정합니다. 기본 값 : 60
	HEALTH_HERO(
			"health.hero",
			60
	),
	//	○ [최초좀비 체력] 최초좀비의 체력을 설정합니다. 기본 값 : 120
	HEALTH_INITIAL_ZOMBIE(
			"health.initial_zombie",
			120
	),
	//	○ [감염자 체력] 감염자의 체력을 설정합니다. 기본 값 : 80
	HEALTH_INFECTEE(
			"health.infectee",
			80
	),
	//	○ [좀비 공격력] 밤이될때 좀비의 추가 근접 공격력. 기본 값 : 50%
	BUFF_ZOMBIE_DAMAGE(
			"buff.zombie.damage",
			50
	),
	//	○ [좀비 속도] 밤이될때 좀비의 추가 이동속도. 기본 값 : 50%
	BUFF_ZOMBIE_SPEED(
			"buff.zombie.speed",
			50
	),
	//	【 추가 】 /zombie config 수정 목록에 <최후의 생존자> 추가 공격력 조절 (기본 +0%)
	BUFF_HUMAN_DAMAGE(
			"buff.human.damage",
			0
	),
	//	【 추가 】 /zombie config 수정 목록에 <최후의 생존자> 이동속도 값 조절 (기본 +50%)
	BUFF_HUMAN_SPEED(
			"buff.human.speed",
			50
	),
	NEXT_SPAWN(
			"next_spawn",
			"random"
	),
	ZOMBIE_INVENTORY_CLEAR(
			"zombie.inventory.clear",
			true
	);

	private final String path;
	private final Object defaultValue;
	private final Cacher nodeHandler;
	private final String[] comments;

	BaseNodes(String path, Object defaultValue, Cacher nodeHandler, String... comments) {
		this.path = path;
		this.defaultValue = defaultValue;
		this.nodeHandler = nodeHandler;
		this.comments = comments;
	}

	BaseNodes(String path, Object defaultValue, String... comments) {
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
