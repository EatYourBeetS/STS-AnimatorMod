package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.interfaces.Hidden;
import eatyourbeets.utilities.GameActionsHelper;

public class DarknessAdrenaline extends AnimatorCard implements Hidden
{
    public static final String ID = CreateFullID(DarknessAdrenaline.class.getSimpleName());

    public DarknessAdrenaline()
    {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0, 0, 1);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainEnergy(magicNumber);
        GameActionsHelper.DrawCard(p, 2);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}