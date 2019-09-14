package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.interfaces.OnEndOfTurnSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class Visions extends UnnamedCard implements OnEndOfTurnSubscriber
{
    public static final String ID = CreateFullID(Visions.class.getSimpleName());

    public Visions()
    {
        super(ID, 0, CardType.POWER, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0,0, 2);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, magicNumber);

        PlayerStatistics.onEndOfTurn.Subscribe(this);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        CardGroup hand = AbstractDungeon.player.hand;
        for (AbstractCard c : hand.group)
        {
            GameActionsHelper.ExhaustCard(c, hand);
        }

        PlayerStatistics.onEndOfTurn.Unsubscribe(this);
    }
}