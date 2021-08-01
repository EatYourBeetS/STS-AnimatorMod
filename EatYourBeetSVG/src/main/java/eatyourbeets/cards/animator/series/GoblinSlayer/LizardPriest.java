package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.replacement.TemporaryRetainPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LizardPriest extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LizardPriest.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public LizardPriest()
    {
        super(DATA);

        Initialize(0, 7, 1);
        SetUpgrade(0, 2, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(1, 1, 0);
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