package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class Millim extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Millim.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetMaxCopies(0)
            .SetSeriesFromClassPackage()
            .ModifyRewards((data, rewards) ->
            {
                final EYBCard copy = data.GetFirstCopy(player.masterDeck);
                if (copy != null)
                {
                    float chances = 0.075f;
                    if (copy.timesUpgraded < 5)
                    {
                        chances += (5 - copy.timesUpgraded) * 0.025f;
                    }

                    GR.Common.Dungeon.TryReplaceFirstCardReward(rewards, chances, true, data);
                }
            });

    public Millim()
    {
        super(DATA);

        Initialize(15, 0, 3);

        SetAffinity_Star(1, 0, 1);

        SetUnique(true, true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded <= 4)
        {
            upgradeDamage(2);
            upgradeMagicNumber(0);
        }
        else
        {
            if (timesUpgraded == 5)
            {
                upgradeMagicNumber(-1);
            }

            upgradeDamage(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.ChannelOrb(new Dark());
        GameActions.Bottom.SelectFromPile(name, magicNumber, p.drawPile)
        .SetOptions(false, false, true)
        .SetFilter(c -> c.costForTurn > 0)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                CostModifiers.For(c).Add(cardID, 1);
            }
        });
    }
}