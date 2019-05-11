package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Guy extends AnimatorCard
{
    public static final String ID = CreateFullID(Guy.class.getSimpleName());

    public Guy()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,4, 1);

        this.baseSecondaryValue = this.secondaryValue = 4;

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.ChooseAndDiscard(this.magicNumber, false);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new NextTurnBlockPower(p, this.secondaryValue), this.secondaryValue);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}