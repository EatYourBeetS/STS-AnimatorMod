package eatyourbeets.cards.unnamed;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;

public class Defend extends UnnamedCard
{
    public static final String ID = Register(Defend.class.getSimpleName());

    public Defend()
    {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);

        Initialize(0, 6);

        this.tags.add(BaseModCardTags.BASIC_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}