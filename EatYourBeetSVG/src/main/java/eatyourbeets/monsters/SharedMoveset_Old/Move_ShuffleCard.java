package eatyourbeets.monsters.SharedMoveset_Old;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_ShuffleCard extends EYBAbstractMove
{
    private final AbstractCard card;
    private final int amount;

    public Move_ShuffleCard(AbstractCard card, int amount)
    {
        this.card = card;
        this.amount = amount;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEBUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.SFX("THUNDERCLAP");

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

        GameActions.Bottom.Add(new MakeTempCardInDiscardAction(this.card, this.amount));
    }
}
