package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.DolaRikuAction;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class DolaRiku extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(DolaRiku.class).SetSeriesFromClassPackage().SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public DolaRiku()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.IsSynergizing && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Draw(1)
            .SetFilter(c -> c.costForTurn == 0 && !GameUtilities.IsHindrance(c), false);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards -> GameActions.Bottom.Add(new DolaRikuAction(cards.get(0), magicNumber)));
    }
}