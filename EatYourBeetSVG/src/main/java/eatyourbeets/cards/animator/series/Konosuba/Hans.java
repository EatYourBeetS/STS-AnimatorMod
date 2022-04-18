package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
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
            .PostInitialize(data -> data.AddPreview(new Status_Slimed(), false));
    public static final int SLIMED_AMOUNT = 3;
    public static final int POISON_AMOUNT = 2;
    public static final int MAX_TEMP_HP = 6;

    public Hans()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Star(1, 1, 0);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(SLIMED_AMOUNT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.MakeCardInDrawPile(new Status_Slimed()).Repeat(SLIMED_AMOUNT);
        GameActions.Bottom.StackPower(new HansPower(p, magicNumber, secondaryValue));
    }

    public static class HansPower extends AnimatorPower
    {
        protected int tempHP;

        public HansPower(AbstractCreature owner, int amount, int tempHP)
        {
            super(owner, Hans.DATA);

            this.tempHP = Math.min(MAX_TEMP_HP, tempHP);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, POISON_AMOUNT, amount, tempHP);
        }

        @Override
        public void OnSamePowerApplied(AbstractPower power)
        {
            this.tempHP = Math.min(MAX_TEMP_HP, tempHP + ((HansPower)power).tempHP);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card)
        {
            super.onAfterCardPlayed(card);

            if (card.type == CardType.STATUS)
            {
                GameActions.Bottom.GainTemporaryHP(tempHP);
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

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(tempHP, Color.GOLD, c.a);
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new HansPower(owner, amount, tempHP);
        }
    }
}