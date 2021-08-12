package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.actions.orbs.EvokeOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class MarisaKirisame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MarisaKirisame.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.Random).SetSeriesFromClassPackage();

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(4, 0, 2);
        SetUpgrade(3, 0, 0);
        SetAffinity_Blue(2, 0, 0);
        SetEthereal(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.EvokeOrb(magicNumber, EvokeOrb.Mode.SameOrb)
                .SetFilter(o -> Lightning.ORB_ID.equals(o.ID));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.LIGHTNING);

        if (isSynergizing)
        {
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
    }
}
