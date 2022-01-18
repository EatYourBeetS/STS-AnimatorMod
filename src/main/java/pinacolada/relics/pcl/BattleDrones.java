package pinacolada.relics.pcl;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

public class BattleDrones extends PCLRelic implements OnSynergySubscriber
{
    public static final String ID = CreateFullID(BattleDrones.class);
    public static final int DAMAGE_AMOUNT = 3;
    public static final int BLOCK_AMOUNT = 1;

    public BattleDrones()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return PCLJUtils.Format(DESCRIPTIONS[0], DAMAGE_AMOUNT, BLOCK_AMOUNT);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        PCLCombatStats.onSynergy.Subscribe(this);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        PCLCombatStats.onSynergy.Unsubscribe(this);
    }

    @Override
    public void OnSynergy(AbstractCard card)
    {
        if (card != null)
        {
            PCLActions.Bottom.Callback(()->
            {
                PCLActions.Bottom.GainBlock(BLOCK_AMOUNT).SetVFX(true, true);
                PCLActions.Bottom.DealDamageToRandomEnemy(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                .SetDamageEffect(enemy ->
                {
                    SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT);
                    final Hitbox source = (player.relics.indexOf(this) / MAX_RELICS_PER_PAGE == relicPage) ? this.hb : player.hb;
                    PCLGameEffects.List.Add(VFX.SmallLaser(source, enemy.hb, Color.CYAN));
                    PCLGameEffects.List.BorderFlash(Color.SKY);
                    return 0f;
                });

                flash();
            });
        }
    }
}