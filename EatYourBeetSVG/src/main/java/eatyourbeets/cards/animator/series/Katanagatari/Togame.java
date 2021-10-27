package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Togame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Togame.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Togame()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 1, 0);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Cycle(name, 1).AddCallback(cards -> {
            if (cards.size() > 0) {
                GameActions.Bottom.Draw(magicNumber);
            }
            GameActions.Bottom.ExhaustFromHand(name, 1, false)
                    .SetOptions(false, false, false)
                    .AddCallback(cards2 ->
                    {
                        for (AbstractCard c : cards2)
                        {
                            CombatStats.Affinities.AddAffinity(Affinity.Orange, secondaryValue);
                            if (info.TryActivateSemiLimited()) {
                                if (!GameUtilities.IsPlayable(c))
                                {
                                    GameActions.Bottom.Motivate(1);
                                }
                                else {
                                    GameActions.Bottom.Draw(1);
                                }
                            }
                        }
                    });
        });
    }
}