package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Defend_Kancolle extends Defend
{
    public static final String ID = Register(Defend_Kancolle.class).ID;

    public Defend_Kancolle()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 5);
        SetUpgrade(0, 3);

        SetHealing(true);
        SetSynergy(Synergies.Kancolle);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainGold(magicNumber);
        }
    }
}