package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class MaesHughes extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MaesHughes.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.FullmetalAlchemist);

    public MaesHughes()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, -1);

        SetAffinity_Green(1);
        SetAffinity_Light(1, 1, 0);

        SetAffinityRequirement(Affinity.Blue, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(Math.floorDiv(p.drawPile.size(), magicNumber));

        if (TryUseAffinity(Affinity.Blue))
        {
            GameActions.Bottom.Motivate();
        }
    }
}