package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;


public class AinzEffect_DamageAll extends AinzEffect
{
    public AinzEffect_DamageAll(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseDamage = ainz.damage = upgraded ? 16 : 10;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActionsHelper.DamageTarget(p, m, ainz.damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
        }
        GameUtilities.UsePenNib();
    }
}