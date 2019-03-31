package eatyourbeets.cards.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;

public class Defend_FullmetalAlchemist extends Defend implements CustomSavable<Integer>
{
    public static final String ID = CreateFullID(Defend_FullmetalAlchemist.class.getSimpleName());

    public Defend_FullmetalAlchemist()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 4);

        this.baseSecondaryValue = this.secondaryValue = GetBaseCooldown();
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        this.isSecondaryValueModified = (this.secondaryValue == 0);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);

        if (ProgressCooldown())
        {
            GameActionsHelper.ChannelOrb(new Frost(), true);
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

    private int GetBaseCooldown()
    {
        return 1;
    }

    private boolean ProgressCooldown()
    {
        boolean activate;
        int newValue;
        if (secondaryValue <= 0)
        {
            newValue = GetBaseCooldown();
            activate = true;
        }
        else
        {
            newValue = secondaryValue - 1;
            activate = false;
        }

        for (AbstractCard c : GetAllInstances())
        {
            AnimatorCard card = (AnimatorCard)c;
            card.baseSecondaryValue = card.secondaryValue = newValue;
            //card.applyPowers();
        }

        return activate;
    }

    @Override
    public Integer onSave()
    {
        return this.secondaryValue;
    }

    @Override
    public void onLoad(Integer integer)
    {
        SetValue(integer);
        initializeDescription();
    }

    protected void SetValue(Integer integer)
    {
        this.baseSecondaryValue = integer != null ? integer : 0;
        this.secondaryValue = this.baseSecondaryValue;
    }
}