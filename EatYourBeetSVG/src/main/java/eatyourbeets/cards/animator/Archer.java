package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.ArcherPower;

public class Archer extends AnimatorCard
{
    public static final String ID = CreateFullID(Archer.class.getSimpleName());

    public Archer()
    {
        super(ID, 1, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);

        Initialize(2,0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArcherPower(p, this.baseDamage), this.baseDamage));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(1);
        }
    }
}