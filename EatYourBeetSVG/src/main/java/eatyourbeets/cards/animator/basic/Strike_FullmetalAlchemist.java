package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;

public class Strike_FullmetalAlchemist extends Strike
{
    public static final String ID = Register(Strike_FullmetalAlchemist.class.getSimpleName());

    public Strike_FullmetalAlchemist()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5,0);

        this.baseSecondaryValue = this.secondaryValue = GetBaseCooldown();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (ProgressCooldown())
        {
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
        }
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
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (ProgressCooldown())
        {
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
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

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            AnimatorCard card = (AnimatorCard)c;
            card.baseSecondaryValue = card.secondaryValue = newValue;
            //card.applyPowers();
        }

        this.baseSecondaryValue = this.secondaryValue = newValue;

        return activate;
    }

    protected void SetValue(Integer integer)
    {
        this.baseSecondaryValue = integer != null ? integer : 0;
        this.secondaryValue = this.baseSecondaryValue;
    }
}