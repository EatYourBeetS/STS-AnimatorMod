package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cocytus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Cocytus.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Cocytus()
    {
        super(DATA);

        Initialize(13, 0, 2, 1);
        SetUpgrade(3, 0, 1, 0);

        SetAffinity_Red(2, 0, 3);

        SetAffinityRequirement(Affinity.Blue, 2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        final AbstractOrb orb = GameUtilities.GetFirstOrb(Frost.ORB_ID);
        if (orb != null)
        {
            orb.showEvokeValue();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.EvokeOrb(1)
        .SetFilter(o -> Frost.ORB_ID.equals(o.ID))
        .AddCallback(orbs ->
        {
            if (orbs.size() > 0)
            {
                GameActions.Bottom.GainForce(magicNumber);
            }
            else
            {
                GameActions.Bottom.ChannelOrb(new Frost());
            }
        });

        if (CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.GainPlatedArmor(secondaryValue);
        }
    }
}