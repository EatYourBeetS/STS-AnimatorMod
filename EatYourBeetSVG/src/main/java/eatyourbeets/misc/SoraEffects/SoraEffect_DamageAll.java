package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.PenNibPower;
import eatyourbeets.GameActionsHelper;


public class SoraEffect_DamageAll extends SoraEffect
{
    public SoraEffect_DamageAll(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseDamage = sora.damage = 6;
        sora.SetMultiDamage(true);
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper.DamageAllEnemies(player, sora.multiDamage, sora.damageTypeForTurn, AbstractGameAction.AttackEffect.SMASH);
        if (player.hasPower(PenNibPower.POWER_ID))
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(player, player, PenNibPower.POWER_ID, 1));
        }
    }
}