package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.FetchFromPile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CowGirl extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CowGirl.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public CowGirl()
    {
        super(DATA);

        Initialize(0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Motivate();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
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

        GameActions.Top.Add(fetchFromPile
        .SetOptions(false, false)
        .SetFilter(c -> c.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(c)));
    }
}