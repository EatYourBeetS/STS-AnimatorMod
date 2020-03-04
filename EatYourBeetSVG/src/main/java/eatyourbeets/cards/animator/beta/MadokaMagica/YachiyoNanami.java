package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YachiyoNanami extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(YachiyoNanami.class).SetPower(2, CardRarity.UNCOMMON);

    public YachiyoNanami()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetEthereal(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new YachiyoNanamiPower(p, magicNumber));
    }

    public static class YachiyoNanamiPower extends AnimatorPower
    {
        public static final int AGILITY_AMOUNT = 1;
        public static final int INTELLECT_AMOUNT = 1;

        public int blockAmount;

        public YachiyoNanamiPower(AbstractPlayer owner, int blockAmount)
        {
            super(owner, YachiyoNanami.DATA);

            this.amount = 1;
            this.blockAmount = blockAmount;

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
            description = FormatDescription(0, amount, blockAmount, AGILITY_AMOUNT, INTELLECT_AMOUNT);
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
                        GameActions.Bottom.GainAgility(INTELLECT_AMOUNT);
                        GameActions.Bottom.GainIntellect(INTELLECT_AMOUNT);
                    }

                    GameActions.Bottom.GainBlock(amount);
                }
            });
        }
    }
}