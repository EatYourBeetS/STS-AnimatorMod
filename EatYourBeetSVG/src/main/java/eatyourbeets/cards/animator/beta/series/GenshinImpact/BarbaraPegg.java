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

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 3, 0);
        SetAffinity_Light(2);
        SetAffinity_Blue(1);


        SetAffinityRequirement(Affinity.Blue, 7);
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
        GameActions.Bottom.HealPlayerLimited(this, magicNumber);
        Water waterOrb = new Water();
        GameActions.Bottom.ChannelOrb(waterOrb).AddCallback(orbs -> {
            int attackingCount = JUtils.Count(GameUtilities.GetIntents(), i -> !i.IsAttacking());
            if (attackingCount > 0 && info.CanActivateSemiLimited && TrySpendAffinity(Affinity.Blue) && info.TryActivateSemiLimited()) {
                GameActions.Bottom.TriggerOrbPassive(waterOrb, attackingCount);
            }
        });

    }
}