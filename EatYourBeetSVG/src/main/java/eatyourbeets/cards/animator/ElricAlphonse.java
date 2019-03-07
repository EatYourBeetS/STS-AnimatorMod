package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class ElricAlphonse extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(ElricAlphonse.class.getSimpleName());

    public ElricAlphonse()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,2);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        ElricAlphonseAlt other = new ElricAlphonseAlt();
        if (this.upgraded)
        {
            other.upgrade();
        }

        GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(other, 1));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);

        if (ProgressBoost())
        {
            GameActionsHelper.ApplyPower(p, p, new FocusPower(p, 1), 1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
            upgradeSecondaryValue(1);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return upgraded ? 3 : 2;
    }
}