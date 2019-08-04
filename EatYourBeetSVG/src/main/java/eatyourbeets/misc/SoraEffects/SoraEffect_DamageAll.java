package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.PenNibPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;


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
        GameActionsHelper.DamageAllEnemies(player, sora.multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SMASH);
        PlayerStatistics.UsePenNib();
    }
}