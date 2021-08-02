package eatyourbeets.cards.animator.status;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class GoblinSoldier extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(GoblinSoldier.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinSoldier()
    {
        super(DATA, true);

        Initialize(0, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.DealDamage(p, p, magicNumber, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL);
        }
    }
}