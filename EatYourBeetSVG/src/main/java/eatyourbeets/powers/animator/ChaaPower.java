package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.beta.AngelBeats.Chaa;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ChaaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ChaaPower.class);

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
                if (card.rarity == AbstractCard.CardRarity.RARE || card.rarity == AbstractCard.CardRarity.UNCOMMON || card.rarity == AbstractCard.CardRarity.SPECIAL) {
                    c =  AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).makeCopy();
                } else if (card.rarity == AbstractCard.CardRarity.COMMON) {
                    c =  AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON).makeCopy();
                } else {
                    c =  AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON).makeCopy();
                }
                addToBot(new MakeTempCardInHandAction(c));
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

