package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.OnAfterCardExhaustedSubscriber;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.DamageRandomEnemy2Action;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class CrowleyEusford extends AnimatorCard
{
    public static final String ID = CreateFullID(CrowleyEusford.class.getSimpleName());

    private int costModifier;

    public CrowleyEusford()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(7, 0, 3);

        SetSynergy(Synergies.OwariNoSeraph);
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

        costModifier = -(PlayerStatistics.getCardsExhaustedThisTurn());

        if (!this.freeToPlayOnce)
        {
            this.setCostForTurn(currentCost + costModifier);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 1; i < magicNumber; i++)
        {
            GameActionsHelper.DamageRandomEnemyWhichActuallyWorks(p, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }

        GameActionsHelper.DamageRandomEnemyWhichActuallyWorks(p, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}