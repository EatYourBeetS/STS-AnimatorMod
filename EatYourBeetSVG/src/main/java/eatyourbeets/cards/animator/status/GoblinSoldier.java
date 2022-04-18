package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class GoblinSoldier extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GoblinSoldier.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinSoldier()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetEndOfTurnPlay(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Draw(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.DealDamage(p, p, magicNumber, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL).CanKill(false);
        }
    }
}