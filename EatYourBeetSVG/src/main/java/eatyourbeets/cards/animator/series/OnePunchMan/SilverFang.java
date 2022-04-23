package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class SilverFang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SilverFang.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.OnePunchMan);

    public SilverFang()
    {
        super(DATA);

        Initialize(0, 6, 1);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1);
        SetAffinity_Light(1);

        SetRetainOnce(true);

        SetAffinityRequirement(Affinity.Red, 2);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (c.type == CardType.ATTACK)
        {
            GameActions.Bottom.ModifyAllInstances(uuid).AddCallback(card ->
            {
                BlockModifiers.For(card).Add(1);
                card.applyPowers();
            });
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        BlockModifiers.For(this).Set(0);

        if (TryUseAffinity(Affinity.Red) && info.TryActivateLimited())
        {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        }
    }
}