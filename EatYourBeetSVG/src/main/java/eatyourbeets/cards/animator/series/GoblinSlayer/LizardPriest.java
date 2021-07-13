package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.common.TemporaryRetainPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LizardPriest extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LizardPriest.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public LizardPriest()
    {
        super(DATA);

        Initialize(0, 7, 1);
        SetUpgrade(0, 2, 1);
        SetScaling(0, 0, 1);

        SetSeries(CardSeries.GoblinSlayer);
        SetAffinity(2, 0, 1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new TemporaryRetainPower(p, magicNumber));

        if (isSynergizing)
        {
            for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.Add(new RemoveAllBlockAction(enemy, p));
                GameActions.Bottom.Add(new GainBlockAction(enemy, p, 1, true));
            }
        }
    }
}