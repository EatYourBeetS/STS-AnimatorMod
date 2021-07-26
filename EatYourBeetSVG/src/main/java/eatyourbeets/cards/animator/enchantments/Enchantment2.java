package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Enchantment2 extends Enchantment
{
    public static final int INDEX = 2;
    public static final EYBCardData DATA = RegisterAura(Enchantment2.class);

    public Enchantment2()
    {
        super(DATA, INDEX);

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