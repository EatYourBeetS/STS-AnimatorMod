package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class SupportDamageAction extends EYBAction
{
    public static AbstractMonster FindBestTarget()
    {
        return JUtils.FindMin(GameUtilities.GetEnemies(true), m -> m.currentHealth);
    }

    public SupportDamageAction(AbstractCreature owner, int output)
    {
        this(owner, FindBestTarget(), output);
    }

    public SupportDamageAction(AbstractCreature owner, AbstractCreature target, int output)
    {
        super(ActionType.DAMAGE);
        this.attackEffect = AttackEffect.NONE;

        Initialize(owner, target, output);
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return target == null || (source != null && source.isDying);
    }

    @Override
    protected void FirstUpdate()
    {
        if (shouldCancelAction())
        {
            Complete();
            return;
        }

        AbstractCreature bestTarget = FindBestTarget();

        if (target != bestTarget || target.isDeadOrEscaped() || target.currentHealth <= 0)
        {
            if (GameUtilities.GetEnemies(true).size() > 0)
            {
                GameActions.Top.Add(new SupportDamageAction(source, bestTarget, amount));
            }

            Complete();
            return;
        }

        GameEffects.List.Add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (shouldCancelAction())
        {
            Complete();
            return;
        }

        if (TickDuration(deltaTime))
        {
            SFX.Play(SFX.ATTACK_DAGGER_6, 0.7f, 0.9f);
            SFX.Play(SFX.ANIMATOR_SUPPORT_DAMAGE, 0.46f, 0.7f, 1.7f);
            SFX.Play(SFX.ANIMATOR_SUPPORT_DAMAGE, 0.26f, 0.46f, 1.7f);
            GameActions.Top.DealDamage(source, target, amount, DamageInfo.DamageType.NORMAL, AttackEffects.NONE)
                    .ApplyPowers(true)
                    .SetDamageEffect(c -> GameEffects.List.Add(VFX.SupportDamage(source.hb, target.hb).SetParticleCount(8 + amount / 3).SetDuration(0.9f, false)).duration * 0.35f)
                    .SetVFX(true, false);

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                GameUtilities.ClearPostCombatActions();
            }
        }
    }
}
