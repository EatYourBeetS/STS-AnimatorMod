package patches.abstractDungeon;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterInfo;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
public class AbstractDungeon_DungeonTransitionSetup
{
    @SpirePrefixPatch
    public static void Prefix()
    {
        for (EYBMonsterInfo e : EYBMonster.Encounters)
        {
            switch (e.type)
            {
                case NORMAL:
                {
                    BaseMod.getMonsterEncounters(e.dungeonID).remove(e.info);
                    BaseMod.getStrongMonsterEncounters(e.dungeonID).remove(e.info);
                    break;
                }
                case ELITE:
                {
                    BaseMod.getEliteEncounters(e.dungeonID).remove(e.info);
                    break;
                }
                case BOSS:
                {
                    BaseMod.getMonsterEncounters(e.dungeonID).remove(e.info);
                    break;
                }
            }
        }

        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
        {
            for (EYBMonsterInfo e : EYBMonster.Encounters)
            {
                switch (e.type)
                {
                    case NORMAL:
                    {
                        BaseMod.addStrongMonsterEncounter(e.dungeonID, e.GetInfo());
                        break;
                    }
                    case ELITE:
                    {
                        BaseMod.addEliteEncounter(e.dungeonID, e.GetInfo());
                        break;
                    }
                    case BOSS:
                    {
                        BaseMod.addBoss(e.dungeonID, e.encounterID, e.GetMapIcon(false), e.GetMapIcon(true));
                        break;
                    }
                }
            }
        }
    }
}