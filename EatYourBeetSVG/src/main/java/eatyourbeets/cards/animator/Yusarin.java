package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Yusarin extends AnimatorCard implements AnimatorResources.Hidden
{
    public static final String ID = CreateFullID(Yusarin.class.getSimpleName());

    public Yusarin()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0, 0,2);

        baseSecondaryValue = secondaryValue = 2;

        SetSynergy(Synergies.Charlotte);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new EnergizedPower(p, 1), 1);
        GameActionsHelper.ApplyPower(p, p, new DrawCardNextTurnPower(p, this.magicNumber), this.magicNumber);

        for (AbstractCard c : com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.get(this.uuid))
        {
            Yusarin other = (Yusarin) c;
            other.baseSecondaryValue = Math.max(0, other.baseSecondaryValue - 1);
            other.secondaryValue = other.baseSecondaryValue;
        }

        if (this.secondaryValue == 0)
        {
            baseSecondaryValue = secondaryValue = 2;

            this.purgeOnUse = true;

            MisaKurobane misaKurobane = new MisaKurobane();
            if (upgraded)
            {
                misaKurobane.upgrade();
            }
            GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(misaKurobane, 1));
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