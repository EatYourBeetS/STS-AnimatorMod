package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.TemporaryBiasPower;

public class ElricAlphonse extends AnimatorCard
{
    public static final String ID = CreateFullID(ElricAlphonse.class.getSimpleName());

    public ElricAlphonse()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        this.isEthereal = true;

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

        GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(other, 1, true, true));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        TemporaryBiasPower power = (TemporaryBiasPower) p.getPower(TemporaryBiasPower.POWER_ID);
        if (power != null)
        {
            power.amount += this.magicNumber;
        }
        else
        {
            p.powers.add(new TemporaryBiasPower(p, this.magicNumber));
        }

        GameActionsHelper.ApplyPower(p, p, new FocusPower(p, this.magicNumber), this.magicNumber);
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