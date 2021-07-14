package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class BattleDrones extends AnimatorRelic implements OnSynergySubscriber
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
        return JUtils.Format(DESCRIPTIONS[0], DAMAGE_AMOUNT, BLOCK_AMOUNT);
    }

    @Override
    protected void Subscribe()
    {
        CombatStats.onSynergy.Subscribe(this);
    }

    @Override
    protected void Unsubscribe()
    {
        CombatStats.onSynergy.Unsubscribe(this);
    }

    @Override
    public void OnSynergy(AbstractCard card)
    {
        if (card != null)
        {
            GameActions.Bottom.Callback(()->
            {
                GameActions.Bottom.GainBlock(BLOCK_AMOUNT).SetVFX(true, true);
                GameActions.Bottom.DealDamageToRandomEnemy(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
                .SetDamageEffect(enemy ->
                {
                    CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
                    GameEffects.List.Add(new SmallLaserEffect(enemy.hb.cX, enemy.hb.cY, player.hb.cX, player.hb.cY));
                    GameEffects.List.Add(new BorderFlashEffect(Color.SKY));
                    return 0f;
                });

                flash();
            });
        }
    }
}