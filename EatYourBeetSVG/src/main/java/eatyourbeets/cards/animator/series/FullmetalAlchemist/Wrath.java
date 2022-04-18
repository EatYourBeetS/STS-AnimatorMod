package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Wrath extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Wrath.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Red), true));

    public Wrath()
    {
        super(DATA);

        Initialize(0, 9, 2);
        SetUpgrade(0, 4, 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainForce(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Red, upgraded));
        }

        GameActions.Bottom.IncreaseScaling(player.hand, player.hand.size(), Affinity.Red, 1).SetFilter(c -> c.uuid != uuid);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return player.exhaustPile.size() >= 7;
    }
}