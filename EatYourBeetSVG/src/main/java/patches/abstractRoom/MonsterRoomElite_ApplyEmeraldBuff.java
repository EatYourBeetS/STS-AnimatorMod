package patches.abstractRoom;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.interfaces.listeners.OnReceiveEmeraldBonusListener;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.utilities.GameActions;

@SpirePatch(clz = MonsterRoomElite.class, method = "applyEmeraldEliteBuff")
public class MonsterRoomElite_ApplyEmeraldBuff
{
    @SpirePrefixPatch
    public static SpireReturn Prefix(MonsterRoomElite __instance)
    {
        if (!AbstractDungeon.getCurrMapNode().hasEmeraldKey || !Settings.isFinalActAvailable ||
        !(__instance.monsters.monsters.size() > 0 && __instance.monsters.monsters.get(0) instanceof EYBMonster))
        {
            return SpireReturn.Continue();
        }

        switch (AbstractDungeon.mapRng.random(0, 3))
        {
            case 0:
            {
                for (AbstractMonster m : __instance.monsters.monsters)
                {
                    GameActions.Bottom.StackPower(new StrengthPower(m,  GetStrengthBonus(m)));
                }
                break;
            }

            case 1:
            {
                for (AbstractMonster m : __instance.monsters.monsters)
                {
                    GameActions.Bottom.Add(new IncreaseMaxHpAction(m, GetMaxHPBonus(m), true));
                }
                break;
            }

            case 2:
            {
                for (AbstractMonster m : __instance.monsters.monsters)
                {
                    GameActions.Bottom.StackPower(new MetallicizePower(m, GetMetallicizeBonus(m)));
                }
                break;
            }

            case 3:
            {
                for (AbstractMonster m : __instance.monsters.monsters)
                {
                    GameActions.Bottom.StackPower(new RegenerateMonsterPower(m, GetRegenBonus(m)));
                }
                break;
            }
        }

        return SpireReturn.Return(null);
    }

    public static int GetStrengthBonus(AbstractMonster m)
    {
        int bonus = AbstractDungeon.actNum + 1;
        if (m instanceof OnReceiveEmeraldBonusListener)
        {
            return ((OnReceiveEmeraldBonusListener)m).GetEmeraldStrengthBonus(bonus);
        }

        return bonus;
    }

    public static int GetRegenBonus(AbstractMonster m)
    {
        int bonus = 1 + AbstractDungeon.actNum * 2;
        if (m instanceof OnReceiveEmeraldBonusListener)
        {
            return ((OnReceiveEmeraldBonusListener)m).GetEmeraldRegenBonus(bonus);
        }

        return bonus;
    }

    public static int GetMetallicizeBonus(AbstractMonster m)
    {
        int bonus = 2 + AbstractDungeon.actNum * 2;
        if (m instanceof OnReceiveEmeraldBonusListener)
        {
            return ((OnReceiveEmeraldBonusListener)m).GetEmeraldMetallicizeBonus(bonus);
        }

        return bonus;
    }

    public static float GetMaxHPBonus(AbstractMonster m)
    {
        float bonus = 0.25f;
        if (m instanceof OnReceiveEmeraldBonusListener)
        {
            return ((OnReceiveEmeraldBonusListener)m).GetEmeraldMaxHPBonus(bonus);
        }

        return bonus;
    }
}
