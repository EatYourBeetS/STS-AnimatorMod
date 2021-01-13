package eatyourbeets.cards.animator.beta.LogHorizon;

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

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 1, 0);

        SetSpellcaster();
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
            {
                if (super.HasSynergy(c))
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new TetoraPower(p, magicNumber));
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
                else if (synCount > 0)
                {
                    synCount -= 1;

                    this.flash();
                }
                else if (synCount == 0)
                {
                    GameActions.Bottom.GainBlock(amount);

                    this.flash();
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