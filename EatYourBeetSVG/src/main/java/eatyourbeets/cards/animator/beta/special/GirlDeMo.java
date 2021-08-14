package eatyourbeets.cards.animator.beta.special;

import basemod.Pair;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.Comparator;

public class GirlDeMo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GirlDeMo.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.AngelBeats);

    public GirlDeMo()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);
        SetAffinity_Orange(2, 0, 0);
        SetAffinity_Light(1, 0, 0);
        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        ArrayList<Pair<ActionT1<Integer>, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(GameActions.Bottom::GainForce, CombatStats.Affinities.GetPowerAmount(Affinity.Red)));
        pairs.add(new Pair<>(GameActions.Bottom::GainAgility, CombatStats.Affinities.GetPowerAmount(Affinity.Green)));
        pairs.add(new Pair<>(GameActions.Bottom::GainIntellect, CombatStats.Affinities.GetPowerAmount(Affinity.Blue)));
        pairs.add(new Pair<>(GameActions.Bottom::GainWillpower, CombatStats.Affinities.GetPowerAmount(Affinity.Orange)));
        pairs.sort(Comparator.comparingInt(Pair::getValue));

        int amount = pairs.get(3).getValue();
        if (amount > 0)
        {
            pairs.get(3).getKey().Invoke(amount);

            for (int i = 2; i >= 0; i--) {
                if (pairs.get(i).getValue().equals(amount))
                {
                    pairs.get(i).getKey().Invoke(amount);
                }
            }
        }
    }
}