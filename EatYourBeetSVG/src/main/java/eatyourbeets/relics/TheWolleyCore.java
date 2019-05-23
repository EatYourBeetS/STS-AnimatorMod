package eatyourbeets.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;

public class TheWolleyCore extends AnimatorRelic
{
    public static final String ID = CreateFullID(TheWolleyCore.class.getSimpleName());

    private static final int CARD_DRAW = 2;
    private static final int BLOCK_AMOUNT = 1;

    public TheWolleyCore()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + CARD_DRAW + DESCRIPTIONS[1] + BLOCK_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActionsHelper.DrawCard(AbstractDungeon.player, CARD_DRAW);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        GameActionsHelper.GainBlock(AbstractDungeon.player, BLOCK_AMOUNT);
    }
}