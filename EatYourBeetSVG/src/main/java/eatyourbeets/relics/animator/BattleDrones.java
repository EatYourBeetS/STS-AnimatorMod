package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.animator.EveDamageAction;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard;

public class BattleDrones extends AnimatorRelic
{
    public static final String ID = CreateFullID(BattleDrones.class.getSimpleName());

    private static final int DAMAGE_AMOUNT = 3;
    private static final int BLOCK_AMOUNT = 1;

    public BattleDrones()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], DAMAGE_AMOUNT, BLOCK_AMOUNT);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.AddToBottom(new GainBlockAction(p, p, BLOCK_AMOUNT, true));
            GameActionsHelper.AddToBottom(new EveDamageAction(p, DAMAGE_AMOUNT));

            this.flash();
        }
    }
}