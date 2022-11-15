package eatyourbeets.cards.animatorClassic.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
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
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
        GameActions.Bottom.TakeDamageAtEndOfTurn(secondaryValue);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ApplyBurning(p, m, magicNumber);
        }
    }
}