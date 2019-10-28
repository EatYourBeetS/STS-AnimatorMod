package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Azekura extends AnimatorCard
{
    public static final String ID = Register(Azekura.class.getSimpleName(), EYBCardBadge.Synergy);

    public Azekura()
    {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,7,2, 2);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.GainBlock(p, this.block);

        GameActionsHelper.ApplyPower(p, p, new EarthenThornsPower(p, secondaryValue), secondaryValue);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(3);
        }
    }
}