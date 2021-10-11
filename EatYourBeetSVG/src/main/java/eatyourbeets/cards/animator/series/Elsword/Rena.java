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

        SetAffinity_Air(1,2,0);
        SetAffinity_Light(1,0,0);

        SetAffinityRequirement(Affinity.Air, 2);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CheckAffinity(Affinity.Air) && CombatStats.TryActivateSemiLimited(cardID))
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
                        GameActions.Bottom.RaiseAirLevel(magicNumber);
                    }
                    else
                    {
                        GameActions.Bottom.ChannelOrb(new Air());
                    }
                });
    }
}