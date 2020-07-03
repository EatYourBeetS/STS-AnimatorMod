package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
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

        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.MakeCardInDiscardPile(new Clumsy());

        if (CombatStats.TryActivateLimited(cardID))
        {
        	GameActions.Bottom.StackPower(new JunSakurada.JunSakuradaPower(p, 1, null));
        }
    }
}