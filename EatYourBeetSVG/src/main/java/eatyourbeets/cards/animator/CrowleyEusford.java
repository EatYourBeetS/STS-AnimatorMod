package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnAfterCardExhaustedSubscriber;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnCostRefreshSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class CrowleyEusford extends AnimatorCard implements OnBattleStartSubscriber, OnAfterCardExhaustedSubscriber, OnCostRefreshSubscriber
{
    public static final String ID = Register(CrowleyEusford.class.getSimpleName(), EYBCardBadge.Synergy);

    private Integer actualCost = null;
    private int lastTurnCount = -1;
    private int costModifier = 0;

    public CrowleyEusford()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(7, 0, 3, 4);

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 1; i < magicNumber; i++)
        {
            GameActionsHelper.DamageRandomEnemyWhichActuallyWorks(p, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }

        GameActionsHelper.DamageRandomEnemyWhichActuallyWorks(p, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        if (HasActiveSynergy() && PlayerStatistics.TryActivateLimited(this.cardID))
        {
            GameActionsHelper.AddToBottom(new HealAction(p, p, secondaryValue));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        OnCostRefresh(this);
    }

    @Override
    public void OnAfterCardExhausted(AbstractCard card)
    {
        if (card != this && AbstractDungeon.player.hand.contains(this))
        {
            modifyCostForTurn(-1);
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onAfterCardExhausted.Subscribe(this);
        OnCostRefresh(this);
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this)
        {
            modifyCostForTurn(-PlayerStatistics.getCardsExhaustedThisTurn());
        }
    }
}