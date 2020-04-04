package eatyourbeets.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterInfo;

public class EYBMonsterInfo
{
    public final AbstractMonster.EnemyType type;
    public final String encounterID;
    public final String dungeonID;
    public final float baseWeight;
    public final MonsterInfo info;

    public EYBMonsterInfo(String dungeonID, AbstractMonster.EnemyType type, String encounterID, float weight)
    {
        this.info = new MonsterInfo(encounterID, weight);
        this.encounterID = encounterID;
        this.dungeonID = dungeonID;
        this.baseWeight = weight;
        this.type = type;
    }

    public MonsterInfo GetInfo()
    {
        info.weight = baseWeight;
        return info;
    }

    public String GetMapIcon(boolean outline)
    {
        return null;
    }
}
