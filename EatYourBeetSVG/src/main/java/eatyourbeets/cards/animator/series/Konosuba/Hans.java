package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Hans extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hans.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(GetClassCard(Slimed.ID), true));
    public static final int SLIMED_AMOUNT = 3;
    public static final int POISON_AMOUNT = 2;
    public static final int RECOVER_AMOUNT = 2;

    public Hans()
    {
        super(DATA);

        Initialize(0, 0, POISON_AMOUNT, SLIMED_AMOUNT);

        SetAffinity_Star(1, 0, 0);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(RECOVER_AMOUNT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.MakeCardInDrawPile(new Status_Slimed(upgraded)).Repeat(secondaryValue);
        GameActions.Bottom.StackPower(new HansPower(p, magicNumber));
    }

    public static class HansPower extends AnimatorPower
    {
        public HansPower(AbstractCreature owner, int amount)
        {
            super(owner, Hans.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, POISON_AMOUNT, amount, RECOVER_AMOUNT);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card)
        {
            super.onAfterCardPlayed(card);

            if (card.type == CardType.STATUS)
            {
                GameActions.Bottom.RecoverHP(RECOVER_AMOUNT).Overheal(true);
                flash();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.ApplyPoison(TargetHelper.RandomEnemy(owner, amount), POISON_AMOUNT);
            flash();
        }
    }
}