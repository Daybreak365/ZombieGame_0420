package sinsa.zombie.stat;

import sinsa.zombie.utils.compat.MaterialX;

public enum Stats {

    LEGENDARY(true, "§1나는 전설이다", "인간 팀으로 승리한 횟수", MaterialX.BLUE_WOOL),
    EXTERMINATION(true, "§1박멸", "좀비 처치 횟수", MaterialX.BLUE_WOOL),
    SADISM(true, "§1가학증", "좀비에게 가한 피해 값", MaterialX.BLUE_WOOL),
    HOST_REMOVAL(true, "§1숙주 제거", "최초 좀비를 처치한 횟수", MaterialX.BLUE_WOOL),
    SWORD_MASTER(true, "§1소드마스터", "근접 무기로 좀비를 처치한 횟수", MaterialX.BLUE_WOOL),
    ROMANTIC_DOCTOR(true, "§1낭만 닥터", "백신으로 좀비를 치료한 횟수", MaterialX.BLUE_WOOL),
    FIRST_KILL(true, "§1퍼스트 킬", "게임에서 처음으로 좀비를 처치한 횟수", MaterialX.BLUE_WOOL),
    HERO_LANDING(true, "§1영웅 등장", "영웅으로 선정된 횟수", MaterialX.BLUE_WOOL),
    SUPER_SOLDIER(true, "§1슈퍼 솔져", "영웅으로 좀비를 처치한 횟수", MaterialX.BLUE_WOOL),
    HOPE_OF_HUMANS(true, "§1인류의 희망", "영웅으로 승리한 횟수", MaterialX.BLUE_WOOL),


    NO_ONE_LEFT(false, "§c아무도 없었다", "좀비 팀으로 승리한 횟수", MaterialX.RED_WOOL),
    BREEDING(false, "§c번식", "인간을 감염시킨 횟수", MaterialX.RED_WOOL),
    MASOCHISM(false, "§c피학증", "인간에게 받은 피해 값", MaterialX.RED_WOOL),
    FIRST_DEATH(false, "§c퍼스트 데스", "게임에서 처음으로 인간을 처치한 횟수", MaterialX.RED_WOOL),
    CHOSEN_GENE(false, "§c선택된 유전자", "최초 좀비로 선정된 횟수", MaterialX.RED_WOOL),
    OUTSTANDING_GENE(false, "§c뛰어난 유전자", "최초 좀비로 인간을 감염시킨 횟수", MaterialX.RED_WOOL),
    VILLAIN(false, "§c빌런", "영웅을 감염시킨 횟수", MaterialX.RED_WOOL),
    CELL_METAMORPHOSIS(false, "§c세포 변성", "좀비로 부활한 횟수", MaterialX.RED_WOOL),
    APOCALYPSE(false, "§c인류의 멸망", "최초 좀비로 승리한 횟수", MaterialX.RED_WOOL);

    private final String display, description;
    private final boolean human;
    private final MaterialX material;
    private final short durability;

    Stats(boolean human, String display, String description, MaterialX material, short durability) {
        this.human = human;
        this.display = display;
        this.description = description;
        this.material = material;
        this.durability = durability;
    }

    Stats(boolean human, String display, String description, MaterialX material) {
        this(human, display, description, material, (short) 0);
    }

    public String getDisplay() {
        return display;
    }

    public String getDescription() {
        return description;
    }

    public MaterialX getMaterial() {
        return material;
    }

    public short getDurability() {
        return durability;
    }
}
