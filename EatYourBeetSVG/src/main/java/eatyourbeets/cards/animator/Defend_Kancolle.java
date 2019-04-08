package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.subscribers.OnRemoveFromDeckSubscriber;

public class Defend_Kancolle extends Defend implements OnRemoveFromDeckSubscriber
{
    public static final String ID = CreateFullID(Defend_Kancolle.class.getSimpleName());

    public Defend_Kancolle()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 40);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
            upgradeMagicNumber(20);
        }
    }

    @Override
    public void OnRemoveFromDeck()
    {
        AbstractDungeon.player.gainGold(this.magicNumber);
    }
}