package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EnvyPower;

public class Envy extends AnimatorCard
{
    public static final String ID = Register(Envy.class.getSimpleName());

    public Envy()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 20);

        SetHealing(true);
        SetSynergy(Synergies.FullmetalAlchemist, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int missingHP = p.maxHealth - p.currentHealth;
        int heal;
        if (upgraded)
        {
            heal = (int)Math.ceil(missingHP * magicNumber / 100f);
        }
        else
        {
            heal = Math.round(missingHP * magicNumber / 100f);
        }

        if (heal > 0)
        {
            p.heal(heal, true);
        }

        GameActionsHelper.ApplyPower(p, p, new EnvyPower(p, 1), 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(5);
        }
    }
}