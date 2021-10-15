package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Rena extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rena.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Rena()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 2, 1);

        SetAffinity_Green(1,2,0);
        SetAffinity_Light(1,0,0);

        SetAffinityRequirement(Affinity.Green, 4);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CheckAffinity(Affinity.Green) && CombatStats.TryActivateSemiLimited(cardID) && TrySpendAffinity(Affinity.Green))
        {
            GameActions.Bottom.GainBlur(secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.EvokeOrb(1)
                .SetFilter(o -> Air.ORB_ID.equals(o.ID))
                .AddCallback(orbs ->
                {
                    if (orbs.size() > 0)
                    {
                        GameActions.Bottom.GainAgility(magicNumber);
                    }
                    else
                    {
                        GameActions.Bottom.ChannelOrb(new Air());
                    }
                });
    }
}