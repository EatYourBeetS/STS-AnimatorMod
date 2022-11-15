package eatyourbeets.cards.animatorClassic.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Millim extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Millim.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.COMMON, EYBAttackType.Elemental);

    public Millim()
    {
        super(DATA);

        Initialize(5, 0, 2);
        SetUpgrade(1, 0, 0);
        SetScaling(1, 1, 1);

        SetUnique(true, true);

    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 2 == 1)
        {
            upgradeMagicNumber(1);
        }
        else
        {
            upgradeDamage(2);
        }

        this.upgradedMagicNumber = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 2));
        }
    }
}