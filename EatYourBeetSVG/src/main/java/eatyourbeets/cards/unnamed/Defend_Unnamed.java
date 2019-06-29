package eatyourbeets.cards.unnamed;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.utilities.GameActionsHelper;

public class Defend_Unnamed extends UnnamedCard
{
    public static final String ID = CreateFullID(Defend_Unnamed.class.getSimpleName());

    public Defend_Unnamed()
    {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF, true);

        Initialize(0, 7,3);

        this.tags.add(BaseModCardTags.BASIC_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.AddToBottom(new LoseHPAction(p, p, this.magicNumber));
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