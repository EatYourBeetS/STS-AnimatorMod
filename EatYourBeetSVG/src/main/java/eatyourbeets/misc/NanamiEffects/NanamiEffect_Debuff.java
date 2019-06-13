package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Debuff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int stack = GetStack(nanami);

        GameActionsHelper.ApplyPower(p, m, new WeakPower(m, stack, false), stack);
        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, stack, false), stack);
    }

    public static void UpdateDescription(Nanami nanami)
    {
        int stack = GetStack(nanami);

        nanami.rawDescription = Desc(WEAK, stack, true) + Desc(VULNERABLE, stack);
    }

    private static int GetStack(Nanami nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }
}