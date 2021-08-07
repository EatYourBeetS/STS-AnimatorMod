package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class BarbaraPegg extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BarbaraPegg.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
    private static final int UNIQUE_THRESHOLD = 4;

    public BarbaraPegg()
    {
        super(DATA);

        Initialize(0, 0, 4, 2);
        SetUpgrade(0, 0, 2, 0);
        SetAffinity_Light(2);
        SetAffinity_Blue(1);


        SetAffinityRequirement(AffinityType.Blue, 2);
        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new RainbowCardEffect());
        GameActions.Bottom.GainBlessing(1, upgraded);
        GameActions.Bottom.GainIntellect(1, upgraded);
        GameActions.Bottom.Heal(magicNumber);
        if (CheckAffinity(AffinityType.Blue) && JUtils.Find(GameUtilities.GetIntents(), i -> !i.isAttacking) == null && CombatStats.TryActivateLimited(cardID)) {
            Water waterOrb = new Water();
            waterOrb.IncreaseBasePassiveAmount(secondaryValue);
            GameActions.Bottom.ChannelOrb(waterOrb);
        }

    }
}