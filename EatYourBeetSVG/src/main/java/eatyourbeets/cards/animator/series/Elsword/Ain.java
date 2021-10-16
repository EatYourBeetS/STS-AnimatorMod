package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.OrbStance;
import eatyourbeets.utilities.GameActions;

public class Ain extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ain.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Ain()
    {
        super(DATA);

        Initialize(3, 0, 3);
        SetUpgrade(2, 0, 0);

        SetAffinity_Air();
        SetAffinity_Water(2);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        boolean hasEmptyOrbs = false;

        for (AbstractOrb orb : player.orbs)
        {
            if (orb instanceof EmptyOrbSlot)
            {
                hasEmptyOrbs = true;
                break;
            }
        }

        if (!hasEmptyOrbs)
        {
            GameActions.Bottom.ChangeStance(OrbStance.STANCE_ID);
        }
    }
}