package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class Amber extends AnimatorCard {
    public static final EYBCardData DATA = Register(Amber.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public Amber() {
        super(DATA);

        Initialize(4, 1, 2, 2);
        SetUpgrade(2, 1, 0);
        SetAffinity_Green(1, 0 ,1);

        SetAffinityRequirement(Affinity.Green, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ApplyLockOn(p,m,magicNumber);
        }
    }
}