package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Hellfire extends UnnamedCard
{
    public static final String ID = CreateFullID(Hellfire.class.getSimpleName());

    public Hellfire()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0,0, 4, 8);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCreature c : PlayerStatistics.GetAllCharacters(true))
        {
            GameActionsHelper.ApplyPower(p, c, new StrengthPower(c, magicNumber), magicNumber);
            GameActionsHelper.ApplyPower(p, c, new BurningPower(c, p, secondaryValue), secondaryValue);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
            upgradeSecondaryValue(1);
        }
    }
}