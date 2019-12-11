package eatyourbeets.monsters.SharedMoveset;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.utilities.GameActions;
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
        GameActions.Bottom.SFX("THUNDERCLAP");

        if (!Settings.FAST_MODE)
        {
            GameActions.Bottom.VFX(new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5F);
            GameActions.Bottom.Add(new FastShakeAction(AbstractDungeon.player, 0.6F, 0.2F));
        }
        else
        {
            GameActions.Bottom.VFX(new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Color.ROYAL, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.1F);
            GameActions.Bottom.Add(new FastShakeAction(AbstractDungeon.player, 0.6F, 0.15F));
        }

        GameActions.Bottom.Add(new MakeTempCardInDiscardAction(this.card, this.amount));
    }
}
