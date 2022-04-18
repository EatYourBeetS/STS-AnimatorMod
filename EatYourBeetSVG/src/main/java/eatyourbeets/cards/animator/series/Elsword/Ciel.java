package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ciel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ciel.class)
            .SetSkill(2, CardRarity.COMMON)
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

        Initialize(0, 4, 8, 2);
        SetUpgrade(0, 1, 0, 1);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Dark(2);

        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(2);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(CombatStats.CanActivatedStarter() ? EYBCardTarget.Normal : EYBCardTarget.None);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlock(block);

        info.SetTempData(CheckAffinity(Affinity.Dark));
        GameActions.Bottom.ModifyAllCopies(Lu.DATA.ID)
        .AddCallback(info, (info2, c) ->
        {
            GameUtilities.IncreaseDamage(c, magicNumber, false);
            if (info2.GetData(false))
            {
                GameUtilities.SetCardTag(c, HASTE, true);
            }
            c.flash();
        });

        if (info.TryActivateStarter())
        {
            GameActions.Bottom.StackPower(new LockOnPower(m, secondaryValue));
        }
    }
}