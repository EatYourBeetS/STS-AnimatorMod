package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;

public class SilverFang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SilverFang.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.OnePunchMan);

    public SilverFang()
    {
        super(DATA);

        Initialize(0, 7, 1);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Light(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
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

        if (info.IsSynergizing && info.TryActivateSemiLimited())
        {
            GameActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
        }
    }
}