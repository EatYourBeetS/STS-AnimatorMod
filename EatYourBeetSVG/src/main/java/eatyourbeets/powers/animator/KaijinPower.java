package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.interfaces.OnAfterCardDrawnSubscriber;
import eatyourbeets.powers.PlayerStatistics;

public class KaijinPower extends AnimatorPower implements OnAfterCardDrawnSubscriber
{
    public static final String POWER_ID = CreateFullID(KaijinPower.class.getSimpleName());

    public KaijinPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PlayerStatistics.onAfterCardDrawn.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PlayerStatistics.onAfterCardDrawn.Unsubscribe(this);
    }

//    @Override
//    public void atEndOfTurn(boolean isPlayer)
//    {
//        super.atEndOfTurn(isPlayer);
//
//        AbstractDungeon.effectsQueue.add(new PowerIconShowEffect(this));
//
//        boolean used = false;
//        AbstractPlayer p = AbstractDungeon.player;
//        RandomizedList<AbstractCard> cards = new RandomizedList<>(p.drawPile.group);
//        while (cards.Count() > 0)
//        {
//            AbstractCard card = cards.Retrieve(AbstractDungeon.miscRng);
//            if (!card.cardID.equals(Nanami.ID))
//            {
//                if (card.baseDamage > 0)
//                {
//                    card.baseDamage += amount;
//                    used = true;
//                }
//                if (card.baseBlock > 0)
//                {
//                    card.baseBlock += amount;
//                    used = true;
//                }
//            }
//
//            if (used)
//            {
//                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
//                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
//                return;
//            }
//        }
//    }

    @Override
    public void OnAfterCardDrawn(AbstractCard card)
    {
        if (card.baseDamage > 0)
        {
            card.baseDamage += amount;
        }
        if (card.baseBlock > 0)
        {
            card.baseBlock += amount;
        }
    }
}
