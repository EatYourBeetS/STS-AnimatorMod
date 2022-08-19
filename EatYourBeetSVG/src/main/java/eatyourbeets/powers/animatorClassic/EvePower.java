package eatyourbeets.powers.animatorClassic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class EvePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EvePower.class);
    public static final int GROWTH_AMOUNT = 1;

    public EvePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorClassicCard card = JUtils.SafeCast(usedCard, AnimatorClassicCard.class);
        if (card != null && card.HasSynergy())
        {
            GameActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(enemy ->
            {
                CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
                GameEffects.List.Add(new BorderFlashEffect(Color.SKY));
                return GameEffects.List.Add(new SmallLaserEffect(enemy.hb.cX, enemy.hb.cY, owner.hb.cX, owner.hb.cY)).duration;
            });

            this.flash();
            this.stackPower(EvePower.GROWTH_AMOUNT);
            this.updateDescription();
        }
    }
}