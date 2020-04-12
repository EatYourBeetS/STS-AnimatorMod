package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;
import eatyourbeets.utilities.WeightedList;
import javafx.util.Pair;

public class OldCoffin extends AnimatorRelic
{
    public static final String ID = CreateFullID(OldCoffin.class);

    private static final WeightedList<Pair<PowerHelper, Integer>> Debuffs = new WeightedList<>();
    private static final int ACTIVATION_THRESHOLD = 4;

    public OldCoffin()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY);

        if (Debuffs.Size() == 0)
        {
            Debuffs.Add(new Pair<>(PowerHelper.Vulnerable, 1), 5);
            Debuffs.Add(new Pair<>(PowerHelper.Weak, 1), 5);
            Debuffs.Add(new Pair<>(PowerHelper.Poison, 3), 3);
            Debuffs.Add(new Pair<>(PowerHelper.Burning, 3), 3);
            Debuffs.Add(new Pair<>(PowerHelper.Constricted, 2), 2);
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
            Pair<PowerHelper, Integer> pair = Debuffs.Retrieve(rng, false);
            GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(null), pair.getKey(), pair.getValue())
            .AddCallback(power ->
            {
                if (power != null && power.owner != null)
                {
                    GameActions.Top.Add(new RelicAboveCreatureAction(power.owner, this));
                }
                else
                {
                    flash();
                }
            });
        }
    }
}