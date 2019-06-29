package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.ShiroPower;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnSynergySubscriber;

public class Shiro extends AnimatorCard
{
    public static final String ID = CreateFullID(Shiro.class.getSimpleName());

    private int costModifier;

    public Shiro()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        costModifier = 0;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        costModifier = 0;
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        int currentCost = (costForTurn - costModifier);

        costModifier = -(PlayerStatistics.getSynergiesThisTurn());

        if (!this.freeToPlayOnce)
        {
            this.setCostForTurn(currentCost + costModifier);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
//        ArrayList<AbstractCard> cards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
//        if (cards.size() > 1 && cards.get(cards.size() - 2).cardID.equals(Sora.ID))
//        {
//            GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.PINK)));
//            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new MasterOfStrategy()));
//            GameActionsHelper.AddToBottom(new WaitRealtimeAction(0.6f));
//        }

        GameActionsHelper.ApplyPower(p, p, new ShiroPower(p), 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(3);
        }
    }
}