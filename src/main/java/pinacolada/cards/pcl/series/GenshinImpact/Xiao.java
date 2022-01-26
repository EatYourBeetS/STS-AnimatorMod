package pinacolada.cards.pcl.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.common.RippledPower;
import pinacolada.powers.special.SwirledPower;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Xiao extends PCLCard
{
    public static final PCLCardData DATA = Register(Xiao.class).SetAttack(3, CardRarity.RARE, PCLAttackType.Piercing, PCLCardTarget.AoE).SetSeriesFromClassPackage(true);

    public Xiao()
    {
        super(DATA);

        Initialize(6, 2, 1, 1);
        SetUpgrade(1, 0, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetHitCount(3);
        SetEthereal(true);
    }

    @Override
    protected float GetInitialDamage()
    {
        int amount = 0;
        for (AbstractCreature c : PCLGameUtilities.GetEnemies(true)) {
            if (PCLGameUtilities.GetPowerAmount(c, SwirledPower.POWER_ID) > 0 || (upgraded && PCLGameUtilities.GetPowerAmount(c, RippledPower.POWER_ID) > 0)) {
                amount += magicNumber;
            }
        }
        return baseDamage + amount;
    }

    @Override
    public int GetXValue() {
        return secondaryValue * player.hand.size();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        SFX.Play(SFX.ATTACK_REAPER);
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE).forEach(d -> d
                .SetDamageEffect((enemy, __) ->
                {
                    float wait = PCLGameEffects.List.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 30f * Settings.scale,
                            500f, 200f, 300f, 3f, Color.FOREST.cpy(), Color.GREEN.cpy())).duration;
                    wait += PCLGameEffects.Queue.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 60f * Settings.scale,
                            500f, 200f, 300f, 5f, Color.FOREST.cpy(), Color.GREEN.cpy())).duration;
                }));
        PCLActions.Bottom.GainBlock(block);

        if (VelocityStance.IsActive()) {
            PCLActions.Bottom.ApplyPoison(TargetHelper.Enemies(), GetXValue());
            PCLActions.Last.Exhaust(this);
        }
        else {
            PCLActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
        }
    }
}

