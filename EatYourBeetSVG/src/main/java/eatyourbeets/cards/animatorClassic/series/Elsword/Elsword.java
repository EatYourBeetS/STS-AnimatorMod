package eatyourbeets.cards.animatorClassic.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;

public class Elsword extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Elsword.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.COMMON);

    public Elsword()
    {
        super(DATA);

        Initialize(13, 0, 2);
        SetUpgrade(4,  0, 0);
        SetScaling(0, 1, 1);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        if (m.hasPower(BurningPower.POWER_ID))
        {
            GameActions.Bottom.Motivate(1);
        }
        else
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Cycle(name, magicNumber);
    }
}