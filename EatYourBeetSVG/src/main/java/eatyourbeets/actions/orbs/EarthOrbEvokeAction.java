package eatyourbeets.actions.orbs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.VFX;
import eatyourbeets.effects.card.RenderProjectilesEffect;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public class EarthOrbEvokeAction extends EYBAction
{
    private final ArrayList<AdvancedTexture> projectiles = new ArrayList<>();
    private final float throwDuration;
    private RenderProjectilesEffect effect;

    public EarthOrbEvokeAction(Earth earth, int damage)
    {
        super(ActionType.DAMAGE, 0.6f);

        this.throwDuration = Settings.FAST_MODE ? 0.14f : 0.21f;
        this.isRealtime = true;

        while (earth.projectiles.size() > 0)
        {
            projectiles.add(earth.projectiles.remove(earth.projectiles.size() - 1));
        }

        Initialize(Mathf.CeilToInt(damage / (float) projectiles.size()));
        if (amount <= 0)
        {
            Complete();
            return;
        }

        final float w = earth.hb.width * 1.1f;
        final float h = earth.hb.height * 0.8f;
        final float angle = 90f / projectiles.size();
        for (int i = 0; i < projectiles.size(); i++)
        {
            AdvancedTexture p = projectiles.get(i);
            p.SetTargetScale(p.scale + 0.35f)
            .SetSpeed(33f, 35f, 210f + (i * 30))
            .SetTargetPosition(earth.cX + (earth.hb.width * 0.35f), earth.cY + (earth.hb.height * 3))
            .SetTargetOffset(w * Mathf.Cos(angle * i), h * Mathf.Sin(angle * i), null)
            .SetTargetRotation(p.target_pos.z + 36000)
            .SetColor(Mathf.SubtractColor(Color.WHITE.cpy(), p.color, false));
        }
        GameEffects.List.Add(effect = new RenderProjectilesEffect(projectiles, 999f, isRealtime));
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        effect.SetDuration(duration + (throwDuration * projectiles.size()), isRealtime);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        if (TickDuration(deltaTime))
        {
            CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", 0.1f);

            for (int i = 0; i < projectiles.size(); i++)
            {
                GameActions.Top.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS, AttackEffect.NONE)
                .SetOptions(true, true)
                .SetDamageEffect(m ->
                {
                    GameEffects.List.Add(VFX.ThrowProjectile(projectiles.remove(0), m.hb)
                    .SetSpread(0.3f, 0.3f)
                    .AddCallback(hb -> GameEffects.Queue.Add(VFX.RockBurst(hb, 1))))
                    .SetDuration(throwDuration, isRealtime);
                    return 0f;
                })
                .SetDuration(throwDuration, isRealtime);
            }

            Complete();
        }
    }
}
