package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.stances.GuardStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kazuma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kazuma.class)
            .SetSkill(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Kazuma()
    {
        super(DATA);

        Initialize(0, 6, 1);
        SetUpgrade(0, 4, 0);

        SetAffinity_Air();

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (IsStarter()) {

            GameActions.Bottom.Cycle(name, 1)
            .ShowEffect(false, false)
            .SetOptions(false, false, false)
            .SetFilter(GameUtilities::IsLowCost)
            .AddCallback(() ->
            GameActions.Bottom.ChangeStance(GuardStance.STANCE_ID));
        }
    }
}