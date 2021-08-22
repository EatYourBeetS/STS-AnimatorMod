package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.utilities.GameActions;

public class SwordMaiden extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SwordMaiden.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public SwordMaiden()
    {
        super(DATA);

        Initialize(0, 0, 6, 2);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.Callback(() ->
        {
            for (int i = player.powers.size() - 1; i >= 0; i--)
            {
                final AbstractPower power = player.powers.get(i);
                if (power.type == AbstractPower.PowerType.DEBUFF)
                {
                    GameActions.Bottom.RemovePower(player, player, power);
                    break;
                }
            }

            final AbstractAffinityPower power = CombatStats.Affinities.GetPower(Affinity.Dark);
            if (power.amount > 0)
            {
                power.reducePower(1);
                GameActions.Bottom.GainBlessing(secondaryValue);
                GameActions.Bottom.GainForce(secondaryValue);
                GameActions.Bottom.GainAgility(secondaryValue);
                GameActions.Bottom.GainIntellect(secondaryValue);
            }
        });
    }
}