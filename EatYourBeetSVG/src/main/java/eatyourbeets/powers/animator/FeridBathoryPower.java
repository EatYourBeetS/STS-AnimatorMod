package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.cards.animator.FeridBathory;
import eatyourbeets.effects.Hemokinesis2Effect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class FeridBathoryPower extends AnimatorPower
{
    private static final int EXHAUST_PILE_THRESHOLD = 20;
    private static final int STRENGTH_GAIN = 8;

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

        GameActionsHelper.AddToBottom(new FeridAction(this));
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        if (AbstractDungeon.player.exhaustPile.size() >= EXHAUST_PILE_THRESHOLD && EffectHistory.TryActivateLimited(FeridBathory.ID))
        {
            GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, STRENGTH_GAIN), STRENGTH_GAIN);

            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(new FeridBathory()));
        }
    }

    private class FeridAction extends AnimatorAction
    {
        private final FeridBathoryPower power;

        public FeridAction(FeridBathoryPower power)
        {
            this.power = power;
            this.amount = power.amount;
        }

        @Override
        public void update()
        {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (target != null)
            {
                GameActionsHelper.VFX(new Hemokinesis2Effect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.2f);
                GameActionsHelper.DamageTarget(owner, target, amount, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE);
                GameActionsHelper.GainTemporaryHP(owner, owner, amount);

                power.flash();
            }

            this.isDone = true;
        }
    }
}