package eatyourbeets.cards.animatorClassic.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.FetchFromPile;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CowGirl extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(CowGirl.class).SetSeriesFromClassPackage().SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public CowGirl()
    {
        super(DATA);

        Initialize(0, 0);

        SetExhaust(true);
        
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Motivate(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        FetchFromPile fetchFromPile;

        if (upgraded)
        {
            fetchFromPile = new FetchFromPile(name, 1, p.drawPile, p.discardPile);
        }
        else
        {
            fetchFromPile = new FetchFromPile(name, 1, p.drawPile);
        }

        GameActions.Bottom.Add(fetchFromPile
        .SetOptions(false, false)
        .SetFilter(c -> c.costForTurn == 0 && !GameUtilities.IsHindrance(c)));
    }
}