package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.JUtils;

public class BattleDrones extends AnimatorRelic
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

//    @Override
//    public void OnSynergy(AbstractCard card)
//    {
//        if (card != null)
//        {
//            GameActions.Bottom.Callback(()->
//            {
//                GameActions.Bottom.GainBlock(BLOCK_AMOUNT).SetVFX(true, true);
//                GameActions.Bottom.DealDamageToRandomEnemy(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
//                .SetDamageEffect(enemy ->
//                {
//                    SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT);
//                    final Hitbox source = (player.relics.indexOf(this) / MAX_RELICS_PER_PAGE == relicPage) ? this.hb : player.hb;
//                    GameEffects.List.Add(VFX.SmallLaser(source, enemy.hb, Color.CYAN));
//                    GameEffects.List.BorderFlash(Color.SKY);
//                    return 0f;
//                });
//
//                flash();
//            });
//        }
//    }
}