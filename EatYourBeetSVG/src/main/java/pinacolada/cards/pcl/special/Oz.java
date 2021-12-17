package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Oz extends PCLCard
{
    public static final PCLCardData DATA = Register(Oz.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.GenshinImpact);

    public Oz()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

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
            PCLActions.Bottom.GainOrbSlots(magicNumber);
        }
        PCLActions.Bottom.StackPower(new OzPower(p, this.secondaryValue));
    }

    public static class OzPower extends PCLPower
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

            if (this.spellcastersPlayed < this.amount && PCLGameUtilities.HasBlueAffinity(card)) {
                PCLActions.Bottom.ChannelOrb(rng.randomBoolean(0.5f) ? new Dark() : new Lightning());
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