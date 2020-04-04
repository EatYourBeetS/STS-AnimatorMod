package eatyourbeets.monsters.SharedMoveset_Old;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_ShuffleDazed extends EYBAbstractMove
{
    private final boolean skipAnimation;
    private final int amount;

    public Move_ShuffleDazed(int amount)
    {
        this(amount, false);
    }

    public Move_ShuffleDazed(int amount, boolean skipAnimation)
    {
        this.amount = amount;
        this.skipAnimation = skipAnimation;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.DEBUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
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

        GameActions.Bottom.Add(new MakeTempCardInDiscardAction(new Dazed(), this.amount));
    }
}