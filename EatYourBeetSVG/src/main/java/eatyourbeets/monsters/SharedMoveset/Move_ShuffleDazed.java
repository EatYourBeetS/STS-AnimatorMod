package eatyourbeets.monsters.SharedMoveset;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;

public class Move_ShuffleDazed extends AbstractMove
{
    private final int amount;

    public Move_ShuffleDazed(int amount)
    {
        this.amount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEBUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.SFX("THUNDERCLAP");

        if (!Settings.FAST_MODE)
        {
            GameActions.Bottom.VFX(new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5F);
            GameActionsHelper_Legacy.AddToBottom(new FastShakeAction(AbstractDungeon.player, 0.6F, 0.2F));
        }
        else
        {
            GameActions.Bottom.VFX(new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.1F);
            GameActionsHelper_Legacy.AddToBottom(new FastShakeAction(AbstractDungeon.player, 0.6F, 0.15F));
        }

        GameActionsHelper_Legacy.AddToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.amount));
    }
}
