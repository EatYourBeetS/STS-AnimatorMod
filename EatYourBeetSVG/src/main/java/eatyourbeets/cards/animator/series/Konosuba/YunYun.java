package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class YunYun extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YunYun.class)
            .SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public YunYun()
    {
        super(DATA);

        Initialize(7, 0);
        SetUpgrade(2, 0);

        SetAffinity_Blue(1, 1, 1);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        GameActions.Bottom.Callback(this::RefreshCost);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.LIGHTNING);

        if (CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
    }

    public void RefreshCost()
    {
        int attacks = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c != this && c.type == CardType.ATTACK)
            {
                attacks += 1;
            }
        }

        CostModifiers.For(this).Set(attacks);
    }
}