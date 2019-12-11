package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.animator.series.OwariNoSeraph.FeridBathory;
import eatyourbeets.effects.Hemokinesis2Effect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class FeridBathoryPower extends AnimatorPower
{
    public static final int EXHAUST_PILE_THRESHOLD = 20;
    public static final int STRENGTH_GAIN = 9;

    public static final String POWER_ID = CreateFullID(FeridBathoryPower.class.getSimpleName());

    public FeridBathoryPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        super.onExhaust(card);

        GameActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(enemy -> AbstractDungeon.effectList.add(new Hemokinesis2Effect(enemy.hb.cX, enemy.hb.cY, owner.hb.cX, owner.hb.cY)));
        GameActions.Bottom.GainTemporaryHP(amount);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        if (AbstractDungeon.player.exhaustPile.size() >= EXHAUST_PILE_THRESHOLD && EffectHistory.TryActivateLimited(FeridBathory.ID))
        {
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(new FeridBathory()));

            GameActions.Bottom.GainStrength(STRENGTH_GAIN);
        }
    }
}