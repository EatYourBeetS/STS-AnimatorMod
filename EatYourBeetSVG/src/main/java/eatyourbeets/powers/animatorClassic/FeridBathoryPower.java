package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.series.OwariNoSeraph.FeridBathory;
import eatyourbeets.effects.vfx.megacritCopy.HemokinesisEffect2;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class FeridBathoryPower extends AnimatorClassicPower
{
    public static final int EXHAUST_PILE_THRESHOLD = 20;
    public static final int FORCE_AMOUNT = 10;

    public static final String POWER_ID = CreateFullID(FeridBathoryPower.class);

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
        .SetDamageEffect(enemy -> GameEffects.List.Add(new HemokinesisEffect2(enemy.hb.cX, enemy.hb.cY, owner.hb.cX, owner.hb.cY)).duration);
        GameActions.Bottom.GainTemporaryHP(amount);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        if (player.exhaustPile.size() >= EXHAUST_PILE_THRESHOLD && CombatStats.TryActivateLimited(FeridBathory.DATA.ID))
        {
            GameEffects.Queue.ShowCardBriefly(new FeridBathory());
            GameActions.Bottom.GainForce(FORCE_AMOUNT);
        }
    }
}