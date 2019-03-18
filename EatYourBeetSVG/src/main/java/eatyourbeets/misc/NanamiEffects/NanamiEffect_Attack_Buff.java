package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Attack_Buff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int block = GetBlock(nanami);
        if (block > 0)
        {
            GameActionsHelper.GainBlock(p, block);
        }

        int strength = GetStrength(nanami);
        if (strength > 0)
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, strength), strength);
        }
    }

    public static void UpdateDescription(Nanami nanami)
    {
        nanami.rawDescription = Desc(BLOCK, GetBlock(nanami), true) + Desc(STRENGTH, GetStrength(nanami));
    }

    private static int GetStrength(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }

    private static int GetBlock(Nanami nanami)
    {
        int modifier = nanami.energyOnUse;

        if (modifier > 0)
        {
            return (modifier + 1) * nanami.block;
        }
        else
        {
            return -1;
        }
    }
}