package eatyourbeets.cards.animator.series.OwariNoSeraph;

import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Shigure extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shigure.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Shigure()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(2, 0, 1, 0);

        SetAffinity_Green(1, 1, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.DaggerSpray()).duration);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (AgilityStance.IsActive())
        {
            GameActions.Bottom.Cycle(name, 1);
        }
    }
}