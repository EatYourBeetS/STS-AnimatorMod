package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnBattleStartSubscriber;
import eatyourbeets.subscribers.OnStartOfTurnPostDrawSubscriber;

public class ChaikaTrabant extends AnimatorCard implements OnBattleStartSubscriber, OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(ChaikaTrabant.class.getSimpleName());

    private int timer;

    public ChaikaTrabant()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(7,0, 4);

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        this.exhaust = true;
        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        this.timer = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        GameActionsHelper.AddToBottom(new StunMonsterAction(m, p));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
            //upgradeDamage(2);
            //upgradeMagicNumber(-1);
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.exhaustPile.contains(this))
        {
            if (timer == 0)
            {
                timer = baseMagicNumber;
                GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.drawPile, p.exhaustPile, true));
            }
            else
            {
                timer -= 1;
            }
        }
    }
}