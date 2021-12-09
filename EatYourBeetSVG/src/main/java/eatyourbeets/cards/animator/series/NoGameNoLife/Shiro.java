package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.EvokeOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shiro extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shiro.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None, true)
            .SetSeriesFromClassPackage();
    public static final int CHARGE_COST = 4;

    public Shiro()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(1);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.Scry(secondaryValue).AddCallback(
                cards -> {
                    if (cards.size() > 0) {
                        GameActions.Bottom.TriggerOrbPassive(cards.size(), true, false);
                    }

                    if (GameUtilities.TrySpendAffinityPower(Affinity.Light, CHARGE_COST)) {
                        GameActions.Bottom.EvokeOrb(secondaryValue, EvokeOrb.Mode.SameOrb);
                    }
                }
        );
    }
}