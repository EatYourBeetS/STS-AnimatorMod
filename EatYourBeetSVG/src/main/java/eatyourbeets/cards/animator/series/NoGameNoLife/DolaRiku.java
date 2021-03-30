package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.DolaRikuAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class DolaRiku extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaRiku.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public DolaRiku()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (isSynergizing && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Draw(1)
            .SetFilter(c -> c.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(c), false);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards -> GameActions.Bottom.Add(new DolaRikuAction(cards.get(0), magicNumber)));
    }
}