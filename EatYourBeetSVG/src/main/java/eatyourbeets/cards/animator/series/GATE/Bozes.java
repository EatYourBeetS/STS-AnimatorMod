package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.BozesPower;
import eatyourbeets.utilities.GameActions;

public class Bozes extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Bozes.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage();

    public Bozes()
    {
        super(DATA);

        Initialize(7, 0, 2, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(2);
        SetAffinity_Light(1, 0, 1);
        SetAffinity_Orange(0,0,1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.Motivate(magicNumber);
        GameActions.Bottom.StackPower(new BozesPower(p, this.secondaryValue));
    }
}