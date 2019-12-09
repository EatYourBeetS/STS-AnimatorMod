package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.common.TemporaryRetainPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class LizardPriest extends AnimatorCard
{
    public static final String ID = Register(LizardPriest.class.getSimpleName(), EYBCardBadge.Synergy);

    public LizardPriest()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 8, 1, 2);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m != null)
        {
            GameActions.Bottom.Add(new RemoveAllBlockAction(m, p));
            GameActions.Bottom.GainBlock(this.magicNumber);
        }

        GameActions.Bottom.GainBlock(this.block);

        if (HasActiveSynergy())
        {
            GameActions.Bottom.StackPower(new TemporaryRetainPower(p, secondaryValue));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}