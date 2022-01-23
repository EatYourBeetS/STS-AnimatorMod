package pinacolada.cards.pcl.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class DolaSchwi extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(DolaSchwi.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Electric)
            .SetSeriesFromClassPackage();

    public DolaSchwi()
    {
        super(DATA);

        Initialize(9, 0, 2, 10);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1, 0, 3);
        SetAffinity_Silver(1);

        SetCooldown(2, 0, __ -> {});
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrb(new Lightning());
        PCLActions.Bottom.ApplyLockOn(p,m,magicNumber);

        DolaSchwi other = (DolaSchwi) makeStatEquivalentCopy();
        if (cooldown.ProgressCooldownAndTrigger(m)) {
            other.baseDamage += secondaryValue;
        }
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        super.OnStartOfTurnPostDraw();
        PCLGameEffects.Queue.ShowCardBriefly(this);
        DoDamage();
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    public void DoDamage() {
        applyPowers();
        if (PCLGameUtilities.GetPowers(TargetHelper.Enemies(), LockOnPower.POWER_ID).size() > 0) {
            PCLActions.Bottom.SFX(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.5f, 0.6f);
            PCLActions.Bottom.BorderFlash(Color.SKY);
            PCLActions.Bottom.VFX(VFX.Mindblast(player.dialogX, player.dialogY), 0.1f);
            for (AbstractMonster m : PCLGameUtilities.GetEnemies(true)) {
                if (PCLGameUtilities.GetPowerAmount(m, LockOnPower.POWER_ID) > 0) {
                    this.calculateCardDamage(m);
                    PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.PSYCHOKINESIS);
                }
            }
            PCLGameUtilities.RemoveDamagePowers();
        }
    }
}