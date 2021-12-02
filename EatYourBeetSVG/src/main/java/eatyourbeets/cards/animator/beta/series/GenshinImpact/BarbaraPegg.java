package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class BarbaraPegg extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BarbaraPegg.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage();
    private static final int UNIQUE_THRESHOLD = 4;

    public BarbaraPegg()
    {
        super(DATA);

        Initialize(0, 0, 4, 2);
        SetUpgrade(0, 0, 3, 0);
        SetAffinity_Light(2);
        SetAffinity_Blue(1);


        SetAffinityRequirement(Affinity.Blue, 4);
        SetHarmonic(true);
        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new RainbowCardEffect());
        GameActions.Bottom.GainWisdom(secondaryValue);
        GameActions.Bottom.HealPlayerLimited(this, magicNumber);
        if (info.CanActivateSemiLimited && TrySpendAffinity(Affinity.Blue) && info.TryActivateSemiLimited()) {
            Water waterOrb = new Water();
            GameActions.Bottom.ChannelOrb(waterOrb);
            if (JUtils.Find(GameUtilities.GetIntents(), i -> !i.IsAttacking()) == null) {
                GameActions.Bottom.TriggerOrbPassive(waterOrb, 1);
            }
        }

    }
}