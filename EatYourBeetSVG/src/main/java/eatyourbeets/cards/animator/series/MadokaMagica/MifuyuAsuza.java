package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MifuyuAsuza extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MifuyuAsuza.class)
            .SetSkill(-1, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public MifuyuAsuza()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1, 0, 0);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.General, 9);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);
        GameActions.Bottom.GainOrbSlots(stacks + magicNumber);
        for (int i = 0; i < stacks + magicNumber; i++) {
            GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
        }

        GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            GameActions.Bottom.ChannelOrbs(GameUtilities::GetRandomCommonOrb, stacks);
        });
    }
}