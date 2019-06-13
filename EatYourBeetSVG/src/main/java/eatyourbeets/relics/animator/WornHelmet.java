package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;

public class WornHelmet extends AnimatorRelic
{
    public static final String ID = CreateFullID(WornHelmet.class.getSimpleName());

    private static final int BLOCK_AMOUNT = 4;
    private static final int DAMAGE_AMOUNT = 3;

    public WornHelmet()
    {
        super(ID, RelicTier.COMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + BLOCK_AMOUNT + DESCRIPTIONS[1] + DAMAGE_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public void atBattleStart()
    {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK_AMOUNT));
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard)
    {
        super.onCardDraw(drawnCard);

        if (drawnCard.type == AbstractCard.CardType.CURSE || drawnCard.type == AbstractCard.CardType.STATUS)
        {
            GameActionsHelper.DamageRandomEnemy(AbstractDungeon.player, DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            this.flash();
        }
    }
}