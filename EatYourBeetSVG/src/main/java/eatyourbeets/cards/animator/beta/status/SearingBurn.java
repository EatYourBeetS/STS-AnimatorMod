package eatyourbeets.cards.animator.beta.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SearingBurn extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(SearingBurn.class).SetStatus(-2, CardRarity.COMMON, EYBCardTarget.ALL);

    public SearingBurn()
    {
        super(DATA, false);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 2, 1);
        SetEthereal(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (!this.dontTriggerOnUseCard)
        {
            this.performAction();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        this.performAction();
    }

    private void performAction() {
        int[] damageMatrix = DamageInfo.createDamageMatrix(magicNumber, true);
        GameActions.Bottom.DealDamage(null, player, magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);

        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.ApplyBurning(null, enemy, secondaryValue);
        }
        GameActions.Bottom.ApplyBurning(null, player, secondaryValue);
    }
}