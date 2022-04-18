package eatyourbeets.cards.effects.NanamiEffects;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Defend extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(p, m, GetDamage(nanami), DamageInfo.DamageType.THORNS, AttackEffects.SLASH_DIAGONAL);
        GameUtilities.RemoveDamagePowers();
    }

    @Override
    public int GetDamage(Nanami nanami)
    {
        return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return "";
    }
}