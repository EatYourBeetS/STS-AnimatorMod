package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.beta.series.RozenMaiden.JunSakurada;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Defend_RozenMaiden extends Defend
{
    public static final String ID = Register(Defend_RozenMaiden.class).ID;

    public Defend_RozenMaiden()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        SetSeries(CardSeries.RozenMaiden);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(this.block);

        if (CombatStats.TryActivateLimited(cardID))
        {
        	GameActions.Bottom.StackPower(new JunSakurada.JunSakuradaPower(p, 1, null));
        }
    }
}