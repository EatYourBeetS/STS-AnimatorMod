package eatyourbeets.misc.CounterIntentEffects;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CounterIntentEffect_Escape extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActions.Bottom.DealDamage(p, m, damage, DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_LIGHT);
            GameUtilities.UsePenNib();
        }

        GameActions.Bottom.ApplyPower(p, new StunMonsterPower(m, 1));
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.Stun(true);
    }

    @Override
    public int GetDamage(EYBCard nanami)
    {
        if (nanami.energyOnUse > 0)
        {
            return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
        }
        else
        {
            return 0;
        }
    }
}