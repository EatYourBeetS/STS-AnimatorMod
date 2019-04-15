package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BurningPower;

public class MisaKurobane extends AnimatorCard
{
    public static final String ID = CreateFullID(MisaKurobane.class.getSimpleName());

    public MisaKurobane()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0, 0,2);

        this.baseSecondaryValue = this.secondaryValue = 2;

        //AddExtendedDescription();

        SetSynergy(Synergies.Charlotte);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.magicNumber), this.magicNumber);
        GameActionsHelper.DrawCard(p, 1);

        for (AbstractCard c : com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.get(this.uuid))
        {
            MisaKurobane other = (MisaKurobane) c;
            other.baseSecondaryValue = Math.max(0, other.baseSecondaryValue - 1);
            other.secondaryValue = other.baseSecondaryValue;
        }

        if (this.secondaryValue == 0)
        {
            baseSecondaryValue = secondaryValue = 2;
            this.purgeOnUse = true;

            Yusarin yusarin = new Yusarin();
            if (upgraded)
            {
                yusarin.upgrade();
            }
            GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(yusarin, 1));
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