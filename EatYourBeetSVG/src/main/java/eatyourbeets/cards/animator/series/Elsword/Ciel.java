package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Ciel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ciel.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Lu(), false))
            .ModifyRewards((data, rewards) ->
            {
                if (data.GetTotalCopies(player.masterDeck) < data.MaxCopies && Lu.DATA.GetTotalCopies(player.masterDeck) > 0)
                {
                    GR.Common.Dungeon.TryReplaceFirstCardReward(rewards, 0.075f, true, data);
                }
            });

    public Ciel()
    {
        super(DATA);

        Initialize(0, 4, 12, 1);
        SetUpgrade(0, 1, 0, 1);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(2);

        SetAffinityRequirement(Affinity.Dark, 1);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ModifyAllCopies(Lu.DATA.ID)
        .AddCallback(info, (info2, c) ->
        {
            final boolean activate;
            if (!info2.HasData())
            {
                activate = info2.SetData(TryUseAffinity(Affinity.Dark));
            }
            else
            {
                activate = info2.GetData(false);
            }

            if (activate)
            {
                GameUtilities.IncreaseDamage(c, magicNumber, false);
                GameUtilities.SetCardTag(c, HASTE, true);
                c.flash();
            }
        });

        if (info.TryActivateStarter())
        {
            GameActions.Bottom.ApplyLockOn(TargetHelper.Enemies(), secondaryValue);
        }
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);
        GameActions.Bottom.ApplyLockOn(TargetHelper.Enemies(), secondaryValue);
    }
}