package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Oz extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Oz.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.GenshinImpact);

    public Oz()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetAffinity_Water(2, 0, 0);
        SetAffinity_Dark(2, 0, 0);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(magicNumber);
        }
        GameActions.Bottom.StackPower(new OzPower(p, this.secondaryValue));
    }

    public static class OzPower extends AnimatorPower
    {
        private int spellcastersPlayed;

        public OzPower(AbstractPlayer owner, int amount)
        {
            super(owner, Oz.DATA);

            this.amount = amount;
            this.spellcastersPlayed = 0;

            updateDescription();
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m) {

            super.onPlayCard(card, m);

            if (this.spellcastersPlayed < this.amount && (GameUtilities.GetAffinityLevel(card, Affinity.Water, false) > 1 || GameUtilities.GetAffinityLevel(card, Affinity.Dark, false) > 1)) {
                GameActions.Bottom.ChannelOrb(rng.randomBoolean(0.5f) ? new Dark() : new Lightning());
                this.spellcastersPlayed += 1;
                updateDescription();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            this.spellcastersPlayed = 0;
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, this.spellcastersPlayed);
        }
    }
}