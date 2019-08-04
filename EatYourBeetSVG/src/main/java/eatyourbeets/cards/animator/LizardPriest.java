package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.common.TemporaryRetainPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class LizardPriest extends AnimatorCard
{
    public static final String ID = CreateFullID(LizardPriest.class.getSimpleName());

    public LizardPriest()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 8, 1);

        this.baseSecondaryValue = this.secondaryValue = 2;

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m != null)
        {
            GameActionsHelper.AddToBottom(new RemoveAllBlockAction(m, p));
            GameActionsHelper.GainBlock(m, this.magicNumber);
        }

        GameActionsHelper.GainBlock(p, this.block);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new TemporaryRetainPower(p, secondaryValue), secondaryValue);
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