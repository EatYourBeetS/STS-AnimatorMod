package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.Hidden;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.YukoShionjiPower;

public class _YukoShionji extends AnimatorCard implements Hidden
{
    public static final String ID = Register(_YukoShionji.class.getSimpleName());

    public _YukoShionji()
    {
        super(ID, 2, CardType.POWER, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        AddExtendedDescription();

        SetSynergy(Synergies.KamisamaNoMemochu);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new YukoShionjiPower(p, 1), 1);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }
}