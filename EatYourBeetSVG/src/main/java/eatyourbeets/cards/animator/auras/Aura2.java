package eatyourbeets.cards.animator.auras;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Aura2 extends Aura
{
    public static final EYBCardData DATA = RegisterAura(Aura2.class);

    public Aura2()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        int heal = CombatStats.SynergiesThisCombat().size();
        if (heal > 0)
        {
            GameActions.Bottom.Heal((heal > magicNumber) ? magicNumber : heal);
        }
    }
}