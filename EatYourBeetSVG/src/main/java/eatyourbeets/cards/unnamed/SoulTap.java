package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.actions._legacy.common.DecreaseMaxHpAction;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.utilities.GameActionsHelper_Legacy; import eatyourbeets.utilities.GameActions;

public class SoulTap extends UnnamedCard
{
    public static final String ID = Register(SoulTap.class.getSimpleName());

    public SoulTap()
    {
        super(ID, 0, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);

        Initialize(0,0, 2, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
//        GameActionsHelper_Legacy.AddToBottom(new DecreaseMaxHpAction(p, 2));
//        GameActionsHelper_Legacy.ApplyPower(p, p, new StrengthPower(p, magicNumber), magicNumber);
//        GameActions.Bottom.Draw(secondaryValue);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeSecondaryValue(1);
        }
    }
}