package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.utilities.GameActionsHelper;

public class Ascent extends UnnamedCard
{
    public static final String ID = CreateFullID(Ascent.class.getSimpleName());

    public Ascent()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0,2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(magicNumber));
        GameActionsHelper.ChannelOrb(new Dark(), true);
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