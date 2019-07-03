package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnCostRefreshSubscriber;
import eatyourbeets.interfaces.OnSynergySubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.ShiroPower;

public class Shiro extends AnimatorCard implements OnBattleStartSubscriber, OnSynergySubscriber, OnCostRefreshSubscriber
{
    public static final String ID = CreateFullID(Shiro.class.getSimpleName());

    public Shiro()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
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

    @Override
    public void triggerWhenDrawn()
    {
        OnCostRefresh(this);
    }

    @Override
    public void OnSynergy(AnimatorCard card)
    {
        if (card != this)
        {
            modifyCostForTurn(-1);
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onSynergy.Subscribe(this);
        OnCostRefresh(this);
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this)
        {
            modifyCostForTurn(-PlayerStatistics.getSynergiesThisTurn());
        }
    }
}