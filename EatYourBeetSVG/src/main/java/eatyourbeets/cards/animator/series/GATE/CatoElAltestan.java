package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CatoElAltestan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CatoElAltestan.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public CatoElAltestan()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Water();
        SetAffinity_Fire();
        SetAffinity_Thunder();

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile)
        .SetOptions(false, true)
        .SetFilter(card -> GameUtilities.HasAffinity(card, Affinity.Water)
                || GameUtilities.HasAffinity(card, Affinity.Fire)
                || GameUtilities.HasAffinity(card, Affinity.Thunder));
    }
}