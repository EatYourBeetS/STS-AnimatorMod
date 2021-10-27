package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CounterIntentEffect_Defend extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(p, m, GetDamage(nanami), DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_LIGHT);
        GameUtilities.UsePenNib();
    }

    @Override
    public int GetDamage(EYBCard nanami)
    {
        return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return "";
    }
}