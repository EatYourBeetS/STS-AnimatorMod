package eatyourbeets.monsters.SharedMoveset;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_ShuffleCard extends AbstractMove
{
    private final AbstractCard card;
    private final int amount;

    public Move_ShuffleCard(AbstractCard card, int amount)
    {
        this.card = card;
        this.amount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.DEBUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActionsHelper.AddToBottom(new SFXAction("THUNDERCLAP"));

        if (!Settings.FAST_MODE)
        {
            GameActionsHelper.AddToBottom(new VFXAction(owner, new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5F));
            GameActionsHelper.AddToBottom(new FastShakeAction(AbstractDungeon.player, 0.6F, 0.2F));
        }
        else
        {
            GameActionsHelper.AddToBottom(new VFXAction(owner, new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.1F));
            GameActionsHelper.AddToBottom(new FastShakeAction(AbstractDungeon.player, 0.6F, 0.15F));
        }

        GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(this.card, this.amount));
    }
}
