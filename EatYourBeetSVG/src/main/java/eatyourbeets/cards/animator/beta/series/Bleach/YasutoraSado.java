package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.stances.MightStance;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YasutoraSado extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YasutoraSado.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public YasutoraSado()
    {
        super(DATA);

        Initialize(2, 0, 5);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(2, 0, 2);
        SetCooldown(2, 0, this::OnCooldownCompleted);
    }


    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy == null || enemy.intent == null) {
            return super.ModifyDamage(enemy, amount);
        }
        return super.ModifyDamage(enemy, (enemy.currentBlock > 0 || GameUtilities.IsDebuffing(enemy.intent)) ? (amount + magicNumber) : amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_EnterStance(VelocityStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(MightStance.STANCE_ID));
        }

        choices.Select(GameActions.Top, 1, m)
                .CancellableFromPlayer(true);

        GameActions.Last.Exhaust(this);
    }
}