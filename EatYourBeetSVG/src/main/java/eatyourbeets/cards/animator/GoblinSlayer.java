package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.actions.ShuffleRandomGoblinAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class GoblinSlayer extends AnimatorCard
{
    public static final String ID = CreateFullID(GoblinSlayer.class.getSimpleName());

    public GoblinSlayer()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(6,6);

        this.retain = true;

        AddExtendedDescription();
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        this.retain = true;

        int turnCount = PlayerStatistics.getTurnCount();
        if (turnCount % 2 == 1)
        {
            int goblins = 1;
            if (turnCount > 3)
            {
                goblins += 1;
            }
            if (turnCount > 7)
            {
                goblins += 1;
            }

            GameActionsHelper.AddToBottom(new ShuffleRandomGoblinAction(goblins));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        this.retain = true;
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (AbstractDungeon.player.exhaustPile.size() * 3));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.AddToBottom(new GoblinSlayerAction());
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
            upgradeDamage(3);
        }
    }

    protected class GoblinSlayerAction extends AnimatorAction
    {
        @Override
        public void update()
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractCard c : p.discardPile.group)
            {
                if (c.type == CardType.STATUS || c.type == CardType.CURSE)
                {
                    GameActionsHelper.ExhaustCard(c, p.discardPile);
                }
            }

            for (AbstractCard c : p.hand.group)
            {
                if (c.type == CardType.STATUS || c.type == CardType.CURSE)
                {
                    GameActionsHelper.ExhaustCard(c, p.hand);
                }
            }

            this.isDone = true;
        }
    }
}