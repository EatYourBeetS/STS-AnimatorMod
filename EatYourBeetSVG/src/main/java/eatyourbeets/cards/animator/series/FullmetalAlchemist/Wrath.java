package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Wrath extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Wrath.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Red), true));

    public Wrath()
    {
        super(DATA);

        Initialize(0, 12, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainForce(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.IncreaseScaling(player.hand, player.hand.size(), Affinity.Red, 1).SetFilter(c -> c.uuid != uuid);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.WaitRealtime(0.3f);
            GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Red, upgraded));
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return player.exhaustPile.size() >= 7;
    }
}