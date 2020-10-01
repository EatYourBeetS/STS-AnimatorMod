package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YachiyoNanami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YachiyoNanami.class).SetPower(2, CardRarity.UNCOMMON);

    public YachiyoNanami()
    {
        super(DATA);

        Initialize(0, 0, YachiyoNanamiPower.BLOCK_AMOUNT);
        SetEthereal(true);

        SetSynergy(Synergies.MadokaMagica);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new YachiyoNanamiPower(p, 1));
    }

    public static class YachiyoNanamiPower extends AnimatorPower
    {
        public static final int GAIN_AMOUNT = 1;
        public static final int BLOCK_AMOUNT = 5;

        public YachiyoNanamiPower(AbstractPlayer owner, int amount)
        {
            super(owner, YachiyoNanami.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, BLOCK_AMOUNT, GAIN_AMOUNT);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            flash();

            GameActions.Bottom.DiscardFromHand(name, amount, false)
            .SetOptions(true, true, true)
            .AddCallback(cards ->
            {
                for (AbstractCard card : cards)
                {
                    if (GameUtilities.IsCurseOrStatus(card))
                    {
                        if (GameUtilities.InStance(ForceStance.STANCE_ID))
                        {
                            GameActions.Bottom.GainForce(GAIN_AMOUNT);
                        }
                        else if (GameUtilities.InStance(AgilityStance.STANCE_ID))
                        {
                            GameActions.Bottom.GainAgility(GAIN_AMOUNT);
                        }
                        else if (GameUtilities.InStance(IntellectStance.STANCE_ID))
                        {
                            GameActions.Bottom.GainIntellect(GAIN_AMOUNT);
                        }
                    }

                    GameActions.Bottom.GainBlock(BLOCK_AMOUNT);
                }
            });
        }
    }
}