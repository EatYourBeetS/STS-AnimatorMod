package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.RandomizedList;

public class Status_Slimed extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Status_Slimed.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None);
    private static final RandomizedList<Affinity> affinities = new RandomizedList<>();

    public Status_Slimed()
    {
        super(DATA);

        Initialize(0, 0);

        SetEndOfTurnPlay(false);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (affinities.Size() == 0)
        {
            affinities.Add(Affinity.Red);
            affinities.Add(Affinity.Green);
            affinities.Add(Affinity.Blue);
        }

        InitializeAffinity(affinities.Retrieve(rng, false), 1, 0, 0);
    }
}