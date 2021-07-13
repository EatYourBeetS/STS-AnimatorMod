package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Evileye extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Evileye.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Evileye()
    {
        super(DATA);

        Initialize(0,0, 1);
        SetCostUpgrade(-1);

        SetSeries(CardSeries.Overlord);
        SetAffinity(0, 0, 2, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (HasSynergy() && CombatStats.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.GainIntellect(2);
            GameActions.Bottom.GainOrbSlots(1);
        }

        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.Reload(name, cards ->
        {
            ArrayList<AbstractOrb> orbs = player.orbs;
            if (orbs.size() > 0)
            {
                for (int i = 0; i < cards.size(); i++)
                {
                    orbs.get(0).onStartOfTurn();
                    orbs.get(0).onEndOfTurn();
                }
            }
        });
    }
}