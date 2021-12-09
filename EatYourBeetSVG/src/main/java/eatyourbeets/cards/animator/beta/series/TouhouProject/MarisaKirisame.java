package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class MarisaKirisame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MarisaKirisame.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(4, 0, 2, 4);
        SetUpgrade(2, 0, 1, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.Blue, 4);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.AddAffinity(Affinity.Blue, magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.LIGHTNING);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
        if (TrySpendAffinity(Affinity.Blue)) {
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
    }
}

