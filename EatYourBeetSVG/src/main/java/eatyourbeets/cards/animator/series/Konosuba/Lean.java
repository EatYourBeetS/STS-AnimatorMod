package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Lean extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lean.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Lean()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Blue(1);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Blue, 3);
        SetAffinityRequirement(Affinity.Green, 3);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        secondaryValue = magicNumber * JUtils.Count(player.hand.group, this::WouldSynergize);
        isSecondaryValueModified = secondaryValue > baseSecondaryValue;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.GainSupportDamage(secondaryValue);
        if (TrySpendAffinity(Affinity.Blue)) {
            GameActions.Bottom.ChannelOrbs(GameUtilities::GetRandomCommonOrb, 1);
        }
        if (TrySpendAffinity(Affinity.Green)) {
            GameActions.Bottom.ChannelOrbs(GameUtilities::GetRandomCommonOrb, 1);
        }
    }
}