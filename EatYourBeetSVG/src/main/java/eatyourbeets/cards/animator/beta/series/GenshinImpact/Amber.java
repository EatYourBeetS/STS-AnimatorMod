package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class Amber extends AnimatorCard {
    public static final EYBCardData DATA = Register(Amber.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public Amber() {
        super(DATA);

        Initialize(3, 1, 2, 1);
        SetUpgrade(2, 1, 0);
        SetAffinity_Green(2, 0 ,0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.ChannelOrb(new Fire());
            GameActions.Bottom.GainAgility(secondaryValue, false);
        }

        if (info.IsSynergizing)
        {
            GameActions.Bottom.StackPower(p, new LockOnPower(m, magicNumber));
        }
    }
}