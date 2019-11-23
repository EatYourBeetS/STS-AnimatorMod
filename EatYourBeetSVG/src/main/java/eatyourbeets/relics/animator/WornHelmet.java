package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.JavaUtilities;

public class WornHelmet extends AnimatorRelic
{
    public static final String ID = CreateFullID(WornHelmet.class.getSimpleName());

    private static final int BLOCK_AMOUNT1 = 4;
    private static final int BLOCK_AMOUNT2 = 1;

    public WornHelmet()
    {
        super(ID, RelicTier.COMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], BLOCK_AMOUNT1, BLOCK_AMOUNT2);
    }

    @Override
    public void atBattleStart()
    {
        this.flash();

        GameActionsHelper.AddToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        GameActionsHelper.GainBlock(AbstractDungeon.player, BLOCK_AMOUNT1);
        this.counter = 0;
    }

    @Override
    public void onRefreshHand()
    {
        super.onRefreshHand();

        this.counter = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group)
        {
            if (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS)
            {
                this.counter += 1;
            }
        }
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        int block = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group)
        {
            if (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS)
            {
                block += BLOCK_AMOUNT2;

                this.flash();
            }
        }

        if (block > 0)
        {
            GameActionsHelper.GainBlock(AbstractDungeon.player, block);
        }
    }
}