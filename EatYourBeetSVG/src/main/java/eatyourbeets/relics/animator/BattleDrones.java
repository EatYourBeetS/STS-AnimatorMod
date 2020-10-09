package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class BattleDrones extends AnimatorRelic
{
    public static final String ID = CreateFullID(BattleDrones.class);
    public static final int DAMAGE_AMOUNT = 3;
    public static final int BLOCK_AMOUNT = 1;

    public BattleDrones()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JUtils.Format(DESCRIPTIONS[0], DAMAGE_AMOUNT, BLOCK_AMOUNT);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
        if (card != null && card.HasSynergy())
        {
            GameActions.Bottom.Callback(this::DealDamage);
        }
    }

    protected void DealDamage()
    {
        GameActions.Bottom.GainBlock(BLOCK_AMOUNT).SetVFX(true, true);
        GameActions.Bottom.DealDamageToRandomEnemy(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(enemy ->
        {
            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
            GameEffects.List.Add(new SmallLaserEffect(enemy.hb.cX, enemy.hb.cY, player.hb.cX, player.hb.cY));
            GameEffects.List.Add(new BorderFlashEffect(Color.SKY));
        });

        flash();
    }
}