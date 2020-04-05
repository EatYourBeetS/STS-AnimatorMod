package eatyourbeets.monsters.SharedMoveset.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Debuff;
import eatyourbeets.utilities.GameActions;

public class EYBMove_ShuffleCard extends EYBMove_Debuff
{
    private final AbstractCard card;
    private CardGroup.CardGroupType groupType = CardGroup.CardGroupType.DISCARD_PILE;
    private boolean skipAnimation = false;

    public EYBMove_ShuffleCard(AbstractCard card, int amount)
    {
        super(amount);

        this.card = card;
    }

    public EYBMove_ShuffleCard SetDestination(CardGroup.CardGroupType group)
    {
        this.groupType = group;

        return this;
    }

    public EYBMove_ShuffleCard SkipAnimation(boolean skipAnimation)
    {
        this.skipAnimation = skipAnimation;

        return this;
    }

    public void QueueActions(AbstractCreature target)
    {
        super.QueueActions(target);

        GameActions.Bottom.SFX("THUNDERCLAP");

        if (!skipAnimation)
        {
            if (!Settings.FAST_MODE)
            {
                GameActions.Bottom.VFX(new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5f);
                GameActions.Bottom.Add(new FastShakeAction(AbstractDungeon.player, 0.6f, 0.2f));
            }
            else
            {
                GameActions.Bottom.VFX(new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.1f);
                GameActions.Bottom.Add(new FastShakeAction(AbstractDungeon.player, 0.6f, 0.15f));
            }
        }

        CardGroup group;
        switch (groupType)
        {
            case DRAW_PILE:
                group = AbstractDungeon.player.drawPile;
                break;
            case MASTER_DECK:
                group = AbstractDungeon.player.masterDeck;
                break;
            case HAND:
                group = AbstractDungeon.player.hand;
                break;
            case DISCARD_PILE:
                group = AbstractDungeon.player.discardPile;
                break;
            case EXHAUST_PILE:
                group = AbstractDungeon.player.exhaustPile;
                break;
            default:
                group = null;
                break;
        }

        if (group != null)
        {
            misc.Calculate();
            for (int i = 0; i < misc.amount; i++)
            {
                GameActions.Bottom.MakeCard(card.makeStatEquivalentCopy(), group);
            }
        }
    }
}
