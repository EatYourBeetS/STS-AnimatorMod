package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CatoElAltestan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CatoElAltestan.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public CatoElAltestan()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Blue(2);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Red, 4);
        SetAffinityRequirement(Affinity.Blue, 4);
        SetAffinityRequirement(Affinity.Green, 4);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null && CheckAffinity(Affinity.Blue))
        {
            GameUtilities.GetIntent(m).AddFreezing();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.ApplyFreezing(player, m, 1);
        }
        if (CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.ApplyBurning(player, m, secondaryValue);
        }
        if (CheckAffinity(Affinity.Green))
        {
            GameActions.Bottom.ChannelOrb(new Aether());
        }

        GameActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile)
        .SetOptions(false, true)
        .SetFilter(GameUtilities::HasBlueAffinity)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Bottom.IncreaseScaling(c, Affinity.Blue, 1);
            }
        });
    }
}