package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;


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
        GameActions.Bottom.DealDamageToAll(sora.multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SMASH);
        GameUtilities.UsePenNib();
    }
}