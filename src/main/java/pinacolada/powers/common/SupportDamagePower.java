package pinacolada.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.utilities.GameUtilities;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class SupportDamagePower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(SupportDamagePower.class);
    protected DamageInfo targetDamageInfo;
    protected AbstractCreature bestTarget;
    protected Hitbox hb;


    public SupportDamagePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        targetDamageInfo = new DamageInfo(owner, amount, DamageInfo.DamageType.NORMAL);

        Initialize(amount);
        final float size = ICON_SIZE * Settings.scale * 1.5f;
        hb = new Hitbox(size, size);
    }

    @Override
    public void updateDescription() {
        UpdateParameters();
        this.description = FormatDescription(0, amount, bestTarget != null ? FormatDescription(1, targetDamageInfo.output, PCLJUtils.ModifyString(bestTarget.name, w -> "#y" + w)): "");
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        UpdateParameters();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        hb.update();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        PCLActions.Bottom.Callback(() -> {
            UpdateParameters();
            if (bestTarget != null) {
                PCLActions.Bottom.DealDamage(bestTarget, targetDamageInfo, AttackEffects.NONE)
                        .SetPiercing(true, false)
                        .ApplyPowers(true)
                        .SetDamageEffect(c -> {
                                    SFX.Play(SFX.ATTACK_DAGGER_6, 0.7f, 0.9f);
                                    SFX.Play(SFX.PCL_SUPPORT_DAMAGE, 0.46f, 0.7f, 1.2f);
                                    SFX.Play(SFX.PCL_SUPPORT_DAMAGE, 0.26f, 0.46f, 1.2f);
                                    return PCLGameEffects.List.Add(VFX.SupportDamage(owner.hb, bestTarget.hb).SetParticleCount(8 + amount / 3).SetDuration(0.9f, false)).duration * 0.35f;
                                })
                        .SetVFX(true, false);
            }
            flashWithoutSound();
        });
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        super.renderIcons(sb, x, y, c);
        if (hb.cX != x || hb.cY != y)
        {
            hb.move(x, y);
        }
        if (hb.hovered && bestTarget != null) {
            bestTarget.renderReticle(sb);
        }
    }


    protected void UpdateParameters() {
        targetDamageInfo.base = amount;
        if (GameUtilities.InBattle(false)) {
            bestTarget = PCLGameUtilities.IsPlayer(owner) ? PCLJUtils.FindMin(PCLGameUtilities.GetEnemies(true), m -> m.currentHealth) : player;
            if (bestTarget != null) {
                targetDamageInfo.applyPowers(owner, bestTarget);
            }
        } else {
            bestTarget = null;
        }
    }
}