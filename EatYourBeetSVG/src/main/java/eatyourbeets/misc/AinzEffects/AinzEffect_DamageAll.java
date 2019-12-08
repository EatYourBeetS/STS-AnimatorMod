package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper2;
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
        ainz.baseDamage = ainz.damage = upgraded ? 18 : 12;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActionsHelper2.DealDamage(ainz, m, AbstractGameAction.AttackEffect.FIRE)
            .SetOptions(true, false);
        }
        GameUtilities.UsePenNib();
    }
}