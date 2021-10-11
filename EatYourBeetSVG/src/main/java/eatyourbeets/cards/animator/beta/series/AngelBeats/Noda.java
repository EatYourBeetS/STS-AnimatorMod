package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.utilities.GameActions;

public class Noda extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noda.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public Noda()
    {
        super(DATA);

        Initialize(12, 0, 1, 1);
        SetUpgrade(3, 0, 1, 0);

        SetAffinity_Fire(1, 1, 0);
        AfterLifeMod.Add(this);

        SetAffinityRequirement(Affinity.Fire, 3);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Exhaust(this);
        GameActions.Bottom.RaiseFireLevel(magicNumber, false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY);

        if (CheckAffinity(Affinity.Fire) || info.IsSynergizing)
        {
            GameActions.Bottom.RaiseFireLevel(secondaryValue, upgraded);
            GameActions.Bottom.RaiseEarthLevel(secondaryValue, upgraded);
        }
    }
}