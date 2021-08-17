package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Togame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Togame.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Togame()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Orange(2);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.Draw(1);

                if (GameUtilities.IsHindrance(cards.get(0)) && CombatStats.TryActivateSemiLimited(cardID))
                {
                    GameActions.Bottom.Motivate();
                }
            }
        });
    }
}