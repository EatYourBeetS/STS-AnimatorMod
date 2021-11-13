package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MarisaKirisame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MarisaKirisame.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(4, 0, 2, 4);
        SetUpgrade(2, 0, 1, 0);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Light(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainIntellect(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.LIGHTNING);
        GameActions.Bottom.GainIntellect(magicNumber);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
        if (GameUtilities.SpendSuperchargedCharge(secondaryValue)) {
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
    }
}

