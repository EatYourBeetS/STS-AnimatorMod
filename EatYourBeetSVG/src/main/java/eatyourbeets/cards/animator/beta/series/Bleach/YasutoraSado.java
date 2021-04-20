package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YasutoraSado extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YasutoraSado.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Normal);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public YasutoraSado()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(3, 0, 0);
        SetScaling(0,0,1);
        SetCooldown(2, 0, this::OnCooldownCompleted);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            if (m == null)
            {
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
                {
                    if (enemy.currentBlock > 0 || IsInflictingNegativeEffect(enemy.intent))
                    {
                        return true;
                    }
                }
            }
            else
            {
                return m.currentBlock > 0 || IsInflictingNegativeEffect(m.intent);
            }
        }

        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
        }

        choices.Select(GameActions.Top, 1, m)
                .CancellableFromPlayer(true);

        GameActions.Last.Exhaust(this);
    }

    private boolean IsInflictingNegativeEffect(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.DEBUFF ||
                intent == AbstractMonster.Intent.DEFEND_DEBUFF || intent == AbstractMonster.Intent.STRONG_DEBUFF);
    }
}