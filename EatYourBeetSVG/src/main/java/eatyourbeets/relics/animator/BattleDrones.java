package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class BattleDrones extends AnimatorRelic implements OnAffinitySealedSubscriber
{
    public static final String ID = CreateFullID(BattleDrones.class);
    public static final int DAMAGE_AMOUNT = 4;

    public BattleDrones()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JUtils.Format(DESCRIPTIONS[0], DAMAGE_AMOUNT);
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        CombatStats.onAffinitySealed.ToggleSubscription(this, enabled);
    }

    @Override
    public void OnAffinitySealed(EYBCard card, boolean manual)
    {
        if (player.hand.contains(card))
        {
            GameActions.Bottom.Callback(() ->
            {
                GameActions.Bottom.DealDamageToRandomEnemy(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                .SetDamageEffect(enemy ->
                {
                    SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT);
                    final Hitbox source = (player.relics.indexOf(this) / MAX_RELICS_PER_PAGE == relicPage) ? this.hb : player.hb;
                    GameEffects.List.Add(VFX.SmallLaser(source, enemy.hb, Color.CYAN));
                    GameEffects.List.BorderFlash(Color.SKY);
                    return 0f;
                });

                flash();
            });
        }
    }
}