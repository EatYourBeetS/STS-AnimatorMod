package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class Amber extends AnimatorCard {
    public static final EYBCardData DATA = Register(Amber.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public Amber() {
        super(DATA);

        Initialize(3, 2, 2);
        SetUpgrade(2, 0, 0);
        SetAffinity_Green(2, 0 ,0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Green(2, 0, 1);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }

        if (HasSynergy())
        {
            GameActions.Bottom.StackPower(p, new LockOnPower(m, magicNumber));
        }
    }
}