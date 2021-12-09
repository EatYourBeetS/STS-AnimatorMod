package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

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
        SetAffinity_Orange(1, 0, 0);

        SetAffinityRequirement(Affinity.Green, 4);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup cGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        GameActions.Bottom.Draw(magicNumber).AddCallback((cards) -> {
            cGroup.group.addAll(cards);
            GameActions.Bottom.ExhaustFromHand(name, 1, false)
                    .SetOptions(false, false, false)
                    .AddCallback(() ->
                    {
                        GameActions.Bottom.Draw(1).AddCallback((cards2) -> {
                            cGroup.group.addAll(cards2);
                            if (info.CanActivateSemiLimited && CheckAffinity(Affinity.Green) && info.TryActivateSemiLimited()) {
                                GameActions.Bottom.Motivate(cGroup, 1);
                            }
                        });
                    });
        });
    }
}