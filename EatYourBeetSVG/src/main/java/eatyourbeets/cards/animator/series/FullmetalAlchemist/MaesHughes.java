package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
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
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(Math.floorDiv(p.drawPile.size(), magicNumber));

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Motivate();
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetPowerAmount(Affinity.Blue) > 0
            && (tryUse ? CombatStats.TryActivateSemiLimited(cardID) : CombatStats.CanActivateSemiLimited(cardID));
    }
}