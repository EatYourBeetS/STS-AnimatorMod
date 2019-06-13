package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.PenNibPower;
import eatyourbeets.utilities.GameActionsHelper;

public class SoraEffect_DamageRandom extends SoraEffect
{
    public SoraEffect_DamageRandom(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseDamage = sora.damage = 5;
        sora.baseMagicNumber = sora.magicNumber = 2;
        sora.SetMultiDamage(false);
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        for (int i = 0; i < sora.magicNumber; i++)
        {
            GameActionsHelper.DamageRandomEnemy(player, sora.damage, sora.damageTypeForTurn, AbstractGameAction.AttackEffect.SMASH);
        }
        if (player.hasPower(PenNibPower.POWER_ID))
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(player, player, PenNibPower.POWER_ID, 1));
        }
    }
}