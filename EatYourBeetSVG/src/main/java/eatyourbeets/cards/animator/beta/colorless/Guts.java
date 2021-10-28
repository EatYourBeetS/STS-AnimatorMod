package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import org.apache.commons.lang3.mutable.MutableInt;

public class Guts extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Guts.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Normal, EYBCardTarget.Random).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Berserk);

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
        GameActions.Bottom.GainForce(forceGain);
        GameActions.Top.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
    }

    private int ExecuteAttack(AbstractMonster m, int inflictDamage) {
        MutableInt forceStacks = new MutableInt(1);
        AbstractMonster mo = m == null ? GameUtilities.GetRandomEnemy(true) : m;
        if (mo != null) {
            GameActions.Top.Add(new VFXAction(new OfferingEffect(), Settings.FAST_MODE ? 0.1F : 0.5F));

            GameActions.Bottom.VFX(VFX.VerticalImpact(mo.hb));
            GameActions.Bottom.DealDamageToRandomEnemy(inflictDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HEAVY).AddCallback(target -> {
                if (GameUtilities.IsDeadOrEscaped(target)) {
                    GameActions.Bottom.TakeDamage(magicNumber, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                    forceStacks.addAndGet(ExecuteAttack(m, inflictDamage + secondaryValue));
                }
            });
        }
        return forceStacks.getValue();
    }
}