package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.cards.animator.Urushihara;
import eatyourbeets.utilities.GameActionsHelper;

public class Laziness extends UnnamedCard
{
    public static final String ID = CreateFullID(Laziness.class.getSimpleName());

    public Laziness()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new WeakPower(p, magicNumber, false), magicNumber);
        GameActionsHelper.AddToTop(new QueueCardAction(new Urushihara(), null));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-1);
        }
    }
}