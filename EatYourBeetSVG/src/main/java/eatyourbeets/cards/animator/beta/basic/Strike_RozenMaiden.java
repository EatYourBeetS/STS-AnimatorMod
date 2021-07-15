package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NightmarePower;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Strike_RozenMaiden extends Strike // implements OnStartOfTurnSubscriber
{
    public static final String ID = Register(Strike_RozenMaiden.class).ID;

    public Strike_RozenMaiden()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0);
        SetUpgrade(2, 0);
        
        SetUnique(true,true);
        SetPurge(true);

        SetSeries(CardSeries.RozenMaiden);
        SetAffinity_Dark(1);
    }
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.StackPower(new NightmarePower(p, 1, this));
        	//CombatStats.onStartOfTurn.Subscribe((Strike_RozenMaiden) makeStatEquivalentCopy());
        }
    }
}


/*
    public Strike_RozenMaiden(int x)
    {
        this();

        x=Math.min(x, 999); // Cap at 999

        for (; x > 0; x --)
        {
            upgrade();
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (GameUtilities.GetAscensionLevel() < 10)
        {
            SetSynergy(null); // to keep "no synergy" under asc10
        }
    }
*/

/*
    @Override
    public void OnStartOfTurn()
    {
    	GameActions.Bottom.MakeCardInHand(new Strike_RozenMaiden(timesUpgraded));
    	
    	CombatStats.onStartOfTurn.Unsubscribe(this);
    }*/








