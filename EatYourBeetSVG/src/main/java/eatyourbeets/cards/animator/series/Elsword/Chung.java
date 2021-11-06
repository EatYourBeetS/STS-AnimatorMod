package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.orbs.RemoveOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Chung extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chung.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Chung()
    {
        super(DATA);

        Initialize(1, 2, 13, 7);
        SetUpgrade(0, 2, 2, 0);

        SetAffinity_Red(1);
        SetAffinity_Orange(1);
        SetAffinity_Blue(0,0,2);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (GameUtilities.GetFirstOrb(Frost.ORB_ID) == null)
        {
            amount += secondaryValue;
        }

        return super.ModifyBlock(enemy, amount);
    }


    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        if (GameUtilities.GetFirstOrb(Frost.ORB_ID) != null) {
            damage += magicNumber;
        }
        return super.ModifyDamage(enemy, damage);
    }


    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ChannelOrb(new Frost());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.ICE);
        GameActions.Bottom.GainBlock(block);

        AbstractOrb orb = GameUtilities.GetFirstOrb(Frost.ORB_ID);
        if (orb != null) {
            GameActions.Bottom.Add(new RemoveOrb(orb));
        }
        else {
            GameActions.Last.Exhaust(this);
        }
    }
}