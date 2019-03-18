package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Strong_Debuff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int stack = GetStack(nanami);
        int poison = GetPoison(nanami);

        GameActionsHelper.ApplyPower(p, m, new WeakPower(m, stack, false), stack);
        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, stack, false), stack);
        GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, poison), poison);

    }

    public static void UpdateDescription(Nanami nanami)
    {
        int stack = GetStack(nanami);

        nanami.rawDescription = Desc(WEAK, stack, true) + Desc(VULNERABLE, stack, true) + Desc(POISON, GetPoison(nanami));
    }

    private static int GetStack(Nanami nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }

    private static int GetPoison(Nanami nanami)
    {
        return 2 + (nanami.energyOnUse * (nanami.upgraded ? 3 : 2));
    }
}