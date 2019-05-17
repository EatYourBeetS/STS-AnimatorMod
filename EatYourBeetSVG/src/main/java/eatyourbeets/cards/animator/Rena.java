package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.RandomCostReductionAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Rena extends AnimatorCard
{
    public static final String ID = CreateFullID(Rena.class.getSimpleName());

    public Rena()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0, 5, 2);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper.AddToBottom(new RandomCostReductionAction(1, false));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, this.magicNumber, false), this.magicNumber);
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