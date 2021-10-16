package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Hans extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hans.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Hans()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetCostUpgrade(-1);

        SetAffinity_Poison(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HansPower(p, magicNumber, secondaryValue));
    }

    public static class HansPower extends AnimatorPower
    {
        protected int tempHP;

        public HansPower(AbstractCreature owner, int poison, int tempHP)
        {
            super(owner, Hans.DATA);

            this.tempHP = tempHP;

            Initialize(poison);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, tempHP);
        }

        @Override
        public void OnSamePowerApplied(AbstractPower power)
        {
            this.tempHP += ((HansPower)power).tempHP;
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(owner), amount);
            GameActions.Bottom.Callback(() ->
            {
                for (AbstractCard c : player.hand.group)
                {
                    if (GameUtilities.HasAffinity(c, Affinity.Poison))
                    {
                        GameActions.Top.GainTemporaryHP(tempHP);
                    }
                }
            });
            this.flash();
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(tempHP, Color.GREEN, c.a);
        }
    }
}