package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import org.apache.commons.lang3.mutable.MutableInt;
import pinacolada.cards.base.*;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Guts extends PCLCard
{
    public static final PCLCardData DATA = Register(Guts.class).SetAttack(3, CardRarity.RARE, PCLAttackType.Brutal, eatyourbeets.cards.base.EYBCardTarget.Random).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Berserk);

    public Guts()
    {
        super(DATA);

        Initialize(34, 0, 7, 7);
        SetUpgrade(6, 0, -1, 1);
        SetExhaust(true);

        SetAffinity_Red(2, 0, 6);
        SetAffinity_Dark(2, 0, 0);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int forceGain = ExecuteAttack(m, damage);
        PCLActions.Bottom.GainMight(forceGain);
        PCLActions.Top.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
    }

    private int ExecuteAttack(AbstractMonster m, int inflictDamage) {
        MutableInt forceStacks = new MutableInt(1);
        AbstractMonster mo = m == null ? PCLGameUtilities.GetRandomEnemy(true) : m;
        if (mo != null) {
            PCLActions.Top.Add(new VFXAction(new OfferingEffect(), Settings.FAST_MODE ? 0.1F : 0.5F));

            PCLActions.Bottom.VFX(VFX.VerticalImpact(mo.hb));
            PCLActions.Bottom.DealDamageToRandomEnemy(inflictDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                    .AddCallback(target -> {
                if (PCLGameUtilities.IsDeadOrEscaped(target)) {
                    PCLActions.Bottom.TakeDamage(magicNumber, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                    forceStacks.addAndGet(ExecuteAttack(m, inflictDamage + secondaryValue));
                }
            });
        }
        return forceStacks.getValue();
    }
}