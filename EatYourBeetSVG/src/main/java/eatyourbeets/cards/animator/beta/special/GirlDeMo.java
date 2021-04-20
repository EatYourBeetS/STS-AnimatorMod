package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;

public class GirlDeMo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GirlDeMo.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public GirlDeMo()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);
        SetExhaust(true);
        SetEthereal(true);

        SetSynergy(Synergies.AngelBeats);
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
        pairs.add(new Pair<>(GameActions.Bottom::GainForce, GameUtilities.GetPowerAmount(ForcePower.POWER_ID)));
        pairs.add(new Pair<>(GameActions.Bottom::GainAgility, GameUtilities.GetPowerAmount(AgilityPower.POWER_ID)));
        pairs.add(new Pair<>(GameActions.Bottom::GainIntellect, GameUtilities.GetPowerAmount(IntellectPower.POWER_ID)));
        pairs.sort(Comparator.comparingInt(Pair::getValue));

        int amount = pairs.get(2).getValue();
        if (amount > 0)
        {
            pairs.get(2).getKey().Invoke(amount);

            if (pairs.get(1).getValue().equals(amount))
            {
                pairs.get(1).getKey().Invoke(amount);
            }

            if (pairs.get(0).getValue().equals(amount))
            {
                pairs.get(0).getKey().Invoke(amount);
            }
        }
    }
}