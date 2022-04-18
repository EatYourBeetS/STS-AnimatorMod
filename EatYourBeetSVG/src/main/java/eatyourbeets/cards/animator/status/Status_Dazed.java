package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Status_Dazed extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Status_Dazed.class)
            .SetStatus(UNPLAYABLE_COST, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Dazed()
    {
        super(DATA);

        Initialize(0, 0);

        SetEndOfTurnPlay(false);
        SetEthereal(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Delayed.Exhaust(this)
            .AddCallback(() -> GameActions.Top.GainCorruption(1));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}