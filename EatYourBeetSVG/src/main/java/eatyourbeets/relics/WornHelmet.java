package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;

public class WornHelmet extends AnimatorRelic
{
    public static final String ID = CreateFullID(WornHelmet.class.getSimpleName());

    private static final int DAMAGE_AMOUNT = 3;

    public WornHelmet()
    {
        super(ID, RelicTier.COMMON, LandingSound.HEAVY);
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