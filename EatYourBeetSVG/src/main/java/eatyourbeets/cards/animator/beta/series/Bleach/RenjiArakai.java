package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class RenjiArakai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RenjiArakai.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public RenjiArakai()
    {
        super(DATA);

        Initialize(10, 0, 3);
        SetUpgrade(1, 0, -1);
        SetScaling(0,1,0);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (HasSynergy())
        {
            SetAttackType(EYBAttackType.Piercing);
        }
        else
        {
            SetAttackType(EYBAttackType.Normal);
        }

        return super.GetDamageInfo();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        if (!AgilityStance.IsActive()){
            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
        }
    }
}