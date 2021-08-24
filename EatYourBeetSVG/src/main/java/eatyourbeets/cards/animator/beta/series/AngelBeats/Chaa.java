package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Chaa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chaa.class).SetPower(3, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public Chaa()
    {
        super(DATA);

        Initialize(0, 6, 1, 0);
        SetUpgrade(0, 4, 0, 0);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Orange(2, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.StackPower(new ChaaPower(p, magicNumber));
    }

    public static class ChaaPower extends AnimatorPower
    {
        public ChaaPower(AbstractCreature owner, int amount)
        {
            super(owner, Chaa.DATA);
            this.amount = amount;
            updateDescription();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            GameActions.Bottom.ExhaustFromHand(name, amount, false)
            .SetOptions(false, false, false)
            .AddCallback(cards ->
            {
                for (AbstractCard card : cards)
                {
                    AbstractCard c;
                    if (GameUtilities.IsHindrance(card))
                    {
                        c = AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON).makeCopy();
                    }
                    else if (card.rarity == AbstractCard.CardRarity.RARE || card.rarity == AbstractCard.CardRarity.UNCOMMON || card.rarity == AbstractCard.CardRarity.SPECIAL)
                    {
                        c = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).makeCopy();
                    }
                    else if (card.rarity == AbstractCard.CardRarity.COMMON)
                    {
                        c = AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON).makeCopy();
                    }
                    else
                    {
                        c = AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON).makeCopy();
                    }
                    GameActions.Bottom.MakeCardInHand(c);
                }
            });
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }
}