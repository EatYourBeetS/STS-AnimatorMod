package eatyourbeets.cards.animatorClassic.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.replacement.TemporaryRetainPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LizardPriest extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(LizardPriest.class).SetSeriesFromClassPackage().SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public LizardPriest()
    {
        super(DATA);

        Initialize(0, 7, 1);
        SetUpgrade(0, 2, 1);
        SetScaling(0, 0, 1);


    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new TemporaryRetainPower(p, magicNumber));

        if (info.IsSynergizing)
        {
            for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.Add(new RemoveAllBlockAction(enemy, p));
                GameActions.Bottom.Add(new GainBlockAction(enemy, p, 1, true));
            }
        }
    }
}