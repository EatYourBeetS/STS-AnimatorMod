package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TukaLunaMarceau.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage().PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public TukaLunaMarceau()
    {
        super(DATA);

        Initialize(0, 2, 3);
        SetUpgrade(0, 3);

        SetAffinity_Green(1, 0, 1);

        SetAffinityRequirement(Affinity.Light, 2);
        SetAffinityRequirement(Affinity.Green, 2);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.currentBlock <= 0)
        {
            GameActions.Bottom.Draw(1);
        }

        GameActions.Bottom.GainBlock(block);

        if (TrySpendAffinity(Affinity.Light, Affinity.Green))
        {
            GameActions.Bottom.CreateThrowingKnives(1);
        }
    }
}