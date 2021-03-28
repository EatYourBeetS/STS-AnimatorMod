package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Tetora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tetora.class).SetPower(0, CardRarity.UNCOMMON);

    public Tetora()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, 2);

        SetSpellcaster();
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        int synCount = 0;

        if (super.cardPlayable(m))
        {
            for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
            {
                if (HasSynergy(c))
                {
                    synCount++;
                }
            }
        }

        return synCount >= magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new TetoraPower(p, secondaryValue));
    }

    public static class TetoraPower extends AnimatorPower
    {
        private int synCount;
        private int baseSynCount = 2;

        public TetoraPower(AbstractPlayer owner, int amount)
        {
            super(owner, Tetora.DATA);

            this.amount = amount;
            synCount = baseSynCount;

            updateDescription();
        }

        @Override
        public void atStartOfTurn()
        {
            this.enabled = false;
            synCount = baseSynCount;
            updateDescription();
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            AnimatorCard card = JUtils.SafeCast(usedCard, AnimatorCard.class);
            if (card != null && card.HasSynergy())
            {
                if (!enabled)
                {
                    enabled = true;
                }
                else {
                    synCount -= 1;

                    if (synCount > 0)
                    {
                        this.flash();
                    }
                    else if (synCount == 0)
                    {
                        GameActions.Top.GainBlock(amount);

                        this.flash();
                    }
                }

                updateDescription();
            }
        }

        @Override
        public void updateDescription()
        {
            int counter = synCount;

            if (counter <= 0)
            {
                description = FormatDescription(0, amount, 0, " This power has already been activated this turn.");
            }
            else
            {
                description = FormatDescription(0, amount, counter, "");
            }
        }
    }
}