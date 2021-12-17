package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.misc.GenericEffects.GenericEffect_EnterStance;
import pinacolada.stances.MightStance;
import pinacolada.stances.VelocityStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YasutoraSado extends PCLCard
{
    public static final PCLCardData DATA = Register(YasutoraSado.class).SetAttack(0, CardRarity.COMMON, PCLAttackType.Normal).SetSeriesFromClassPackage();

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
        return super.ModifyDamage(enemy, (enemy.currentBlock > 0 || PCLGameUtilities.IsDebuffing(enemy.intent)) ? (amount + magicNumber) : amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_EnterStance(VelocityStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(MightStance.STANCE_ID));
        }

        choices.Select(PCLActions.Top, 1, m)
                .CancellableFromPlayer(true);

        PCLActions.Last.Exhaust(this);
    }
}