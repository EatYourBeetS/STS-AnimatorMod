package eatyourbeets.cards.animatorClassic.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Genos extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Genos.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged);

    public Genos()
    {
        super(DATA);

        Initialize(14, 0, 3, 4);
        SetUpgrade(4, 0, 0, 0);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.TakeDamageAtEndOfTurn(secondaryValue);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ApplyBurning(p, m, magicNumber);
        }
    }
}