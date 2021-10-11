package eatyourbeets.cards.animator.beta.special;

import basemod.Pair;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.Comparator;

public class GirlDeMo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GirlDeMo.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.AngelBeats);

    public GirlDeMo()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(2, 0, 0);
        SetHarmonic(true);
        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameUtilities.IncreaseSuperchargedCharge(magicNumber);

        ArrayList<Pair<ActionT1<Integer>, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(GameActions.Bottom::RaiseFireLevel, CombatStats.Affinities.GetPowerAmount(Affinity.Fire)));
        pairs.add(new Pair<>(GameActions.Bottom::RaiseAirLevel, CombatStats.Affinities.GetPowerAmount(Affinity.Air)));
        pairs.add(new Pair<>(GameActions.Bottom::RaiseWaterLevel, CombatStats.Affinities.GetPowerAmount(Affinity.Water)));
        pairs.add(new Pair<>(GameActions.Bottom::RaiseEarthLevel, CombatStats.Affinities.GetPowerAmount(Affinity.Earth)));
        pairs.add(new Pair<>(GameActions.Bottom::RaiseEarthLevel, CombatStats.Affinities.GetPowerAmount(Affinity.Light)));
        pairs.sort(Comparator.comparingInt(Pair::getValue));

        int amount = pairs.get(4).getValue();
        if (amount > 0)
        {
            pairs.get(3).getKey().Invoke(amount);

            for (int i = 3; i >= 0; i--) {
                if (pairs.get(i).getValue().equals(amount))
                {
                    pairs.get(i).getKey().Invoke(amount);
                }
            }
        }
    }
}