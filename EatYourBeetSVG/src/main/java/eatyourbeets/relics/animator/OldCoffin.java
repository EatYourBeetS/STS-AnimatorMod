package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;
import eatyourbeets.utilities.TupleT2;
import eatyourbeets.utilities.WeightedList;

public class OldCoffin extends AnimatorRelic
{
    public static final String ID = CreateFullID(OldCoffin.class);

    private static final WeightedList<TupleT2<PowerHelper, Integer>> Debuffs = new WeightedList<>();
    private static final int ACTIVATION_THRESHOLD = 4;

    public OldCoffin()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY);

        if (Debuffs.Size() == 0)
        {
            Debuffs.Add(new TupleT2<>(PowerHelper.Vulnerable, 1), 5);
            Debuffs.Add(new TupleT2<>(PowerHelper.Weak, 1), 5);
            Debuffs.Add(new TupleT2<>(PowerHelper.Shackles, 2), 4);
            Debuffs.Add(new TupleT2<>(PowerHelper.Poison, 3), 3);
            Debuffs.Add(new TupleT2<>(PowerHelper.Burning, 3), 3);
            Debuffs.Add(new TupleT2<>(PowerHelper.Freezing, 3), 3);
            Debuffs.Add(new TupleT2<>(PowerHelper.Blinded, 2), 2);
            Debuffs.Add(new TupleT2<>(PowerHelper.Constricted, 2), 2);
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetEnabled(false);
        SetCounter(0);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
        SetCounter(-1);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        if (SetEnabled(AddCounter(1) > ACTIVATION_THRESHOLD))
        {
            TupleT2<PowerHelper, Integer> pair = Debuffs.Retrieve(rng, false);
            GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(null), pair.V1, pair.V2)
            .AddCallback(power -> GameActions.Top.Add(new RelicAboveCreatureAction(power.owner, this)));

            flash();
        }
    }
}