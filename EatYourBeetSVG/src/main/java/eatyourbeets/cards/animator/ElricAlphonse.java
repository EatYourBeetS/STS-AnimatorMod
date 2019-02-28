package eatyourbeets.cards.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;

public class ElricAlphonse extends AnimatorCard_Cooldown implements CustomSavable<Integer>
{
    public static final String ID = CreateFullID(ElricAlphonse.class.getSimpleName());

    public ElricAlphonse()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0);

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
        if (ProgressCooldown())
        {
            GameActionsHelper.ApplyPower(p, p, new FocusPower(p, 1), 1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            if (secondaryValue > 0)
            {
                baseSecondaryValue = secondaryValue = secondaryValue - 1;
            }
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return upgraded ? 1 : 2;
    }
}