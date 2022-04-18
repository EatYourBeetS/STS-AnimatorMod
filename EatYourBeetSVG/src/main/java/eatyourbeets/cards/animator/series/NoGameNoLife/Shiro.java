package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shiro extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shiro.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();

    public Shiro()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);

        SetAffinity_Blue(2);
        SetAffinity_Light(2);

        SetAffinityRequirement(Affinity.Blue, 5);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainIntellect(1, upgraded);
        GameActions.Bottom.Motivate();

        if (CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.Callback(() ->
            {
                ReduceBlockAndDamage(player.drawPile, magicNumber);
                ReduceBlockAndDamage(player.hand, magicNumber);
                ReduceBlockAndDamage(player.discardPile, magicNumber);
                ReduceBlockAndDamage(player.exhaustPile, magicNumber);
                ReduceBlockAndDamage(CombatStats.PurgedCards, magicNumber);
            });

            GameActions.Bottom.SFX(SFX.TURN_EFFECT, 0.75f, 0.75f);
            GameUtilities.ModifyCardDrawPerTurn(-1, 1);
            GameUtilities.ModifyEnergyGainPerTurn(secondaryValue, 1);
        }
    }

    protected void ReduceBlockAndDamage(CardGroup group, int amount)
    {
        for (AbstractCard c : group.group)
        {
            if (GameUtilities.HasBlueAffinity(c))
            {
                if (c.baseDamage > 0)
                {
                    DamageModifiers.For(c).Add(cardID, -amount);
                }
                if (c.baseBlock > 0)
                {
                    BlockModifiers.For(c).Add(cardID, -amount);
                }
            }
        }
    }
}