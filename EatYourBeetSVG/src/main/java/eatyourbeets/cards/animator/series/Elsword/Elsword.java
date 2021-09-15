package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;

public class Elsword extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Elsword.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Elsword()
    {
        super(DATA);

        Initialize(13, 0, 2);
        SetUpgrade(3,  0, 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Light(1);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL).SetVFXColor(Color.RED);

        if (m.hasPower(BurningPower.POWER_ID))
        {
            GameActions.Bottom.Motivate();
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