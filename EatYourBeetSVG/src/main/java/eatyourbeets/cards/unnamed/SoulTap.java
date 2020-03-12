package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class SoulTap extends UnnamedCard
{
    public static final EYBCardData DATA = Register(SoulTap.class).SetSkill(0, CardRarity.BASIC, EYBCardTarget.None);

    public SoulTap()
    {
        super(DATA);

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