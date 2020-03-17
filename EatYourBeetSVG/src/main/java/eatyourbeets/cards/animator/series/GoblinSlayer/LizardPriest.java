package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.TemporaryRetainPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LizardPriest extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LizardPriest.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public LizardPriest()
    {
        super(DATA);

        Initialize(0, 7, 1);
        SetUpgrade(0, 2, 1);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new TemporaryRetainPower(p, magicNumber));

        if (HasSynergy())
        {
            for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
            {
                GameActions.Bottom.Add(new RemoveAllBlockAction(enemy, p));
                GameActions.Bottom.Add(new GainBlockAction(enemy, p, 1, true));
            }
        }
    }
}