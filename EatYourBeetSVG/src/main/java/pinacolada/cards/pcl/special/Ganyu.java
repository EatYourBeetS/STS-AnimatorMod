package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.status.Status_Frostbite;
import pinacolada.effects.VFX;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Ganyu extends PCLCard
{
    public static final PCLCardData DATA = Register(Ganyu.class).SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Ice).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data ->
                    data.AddPreview(new SheerCold(), false)
                        .AddPreview(new Status_Frostbite(), false));

    public Ganyu()
    {
        super(DATA);

        Initialize(10, 0, 8, 3);
        SetUpgrade(4, 0, 1);
        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Silver(1, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            return super.ModifyDamage(enemy, amount + PCLGameUtilities.GetPowerAmount(enemy, LockOnPower.POWER_ID) * magicNumber);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.MakeCardInHand(new Status_Frostbite())
                .SetDuration(Settings.ACTION_DUR_XFAST, true);
        int amount = PCLGameUtilities.GetPowerAmount(m, LockOnPower.POWER_ID);
        PCLActions.Bottom.DealCardDamage(this, m, GR.Enums.AttackEffect.ICE).forEach(d ->
                d.SetDamageEffect(enemy -> PCLGameEffects.List.Add(VFX.Claw(enemy.hb, Color.SKY,Color.SKY)).duration * 0.3f));
        if (amount > 0) {
            PCLActions.Top.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);
            PCLActions.Top.VFX(VFX.VerticalImpact(m.hb).SetColor(Color.BLUE));
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Player(), PCLGameUtilities.GetPowerAmount(m, LockOnPower.POWER_ID));
            if (amount > secondaryValue && info.TryActivateLimited()) {
                AbstractCard c = new SheerCold();
                c.applyPowers();
                PCLActions.Bottom.PlayCopy(c, null);
            }
        }
    }
}