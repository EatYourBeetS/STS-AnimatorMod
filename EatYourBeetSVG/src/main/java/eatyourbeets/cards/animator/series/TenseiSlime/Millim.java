package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Millim extends AnimatorCard implements OnReceiveRewardsListener
{
    public static final EYBCardData DATA = Register(Millim.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetMaxCopies(0)
            .SetSeriesFromClassPackage()
            .ModifyRewards((data, rewards) ->
            {
                final EYBCard copy = data.GetFirstCopy(player.masterDeck);
                if (copy != null)
                {
                    GR.Common.Dungeon.TryReplaceFirstCardReward(rewards, (5 - Math.min(4, copy.timesUpgraded / 2)) * 0.05f, true, data);
                }
            });

    public Millim()
    {
        super(DATA);

        Initialize(6, 0, 2);

        SetAffinity_Star(1, 0, 1);

        SetAffinityRequirement(Affinity.General, 4);
        SetUnique(true, true);
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards, boolean normalRewards)
    {
        if (normalRewards)
        {
            GR.Common.Dungeon.TryReplaceFirstCardReward(rewards, (5 - Math.min(4, timesUpgraded / 2)) * 0.05f, c -> c.rarity == CardRarity.COMMON, DATA);
        }
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 1)
        {
            upgradeMagicNumber(1);
            upgradeDamage(1);
        }
        else
        {
            upgradeMagicNumber(0);
            upgradeDamage(2);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainRandomAffinityPower(1, false);
        }

        if (CheckAffinity(Affinity.General))
        {
            GameActions.Bottom.ChannelOrb(new Chaos());
        }
    }
}