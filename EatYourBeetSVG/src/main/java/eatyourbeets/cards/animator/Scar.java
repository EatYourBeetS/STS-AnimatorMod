package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.animator.ScarAction;
import eatyourbeets.actions.common.DrawSpecificCardAction;
import eatyourbeets.actions.common.OnTargetDeadAction;
import eatyourbeets.actions.common.PiercingDamageAction;
import eatyourbeets.actions.common.RefreshHandLayoutAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnEvokeOrbSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Scar extends AnimatorCard implements OnBattleStartSubscriber, OnEvokeOrbSubscriber
{
    public static final String ID = Register(Scar.class.getSimpleName(), EYBCardBadge.Special);

    public Scar()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(12,0);

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        PiercingDamageAction damageAction = new PiercingDamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE);
        GameActionsHelper.AddToBottom(new OnTargetDeadAction(m, damageAction, new ScarAction(p), true));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    @Override
    public void OnEvokeOrb(AbstractOrb orb)
    {
        if (AbstractDungeon.player.drawPile.contains(this) || AbstractDungeon.player.hand.contains(this))
        {
            modifyCostForTurn(-1);
            this.flash();

            GameActionsHelper.AddToBottom(new DrawSpecificCardAction(this));
            GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onEvokeOrb.Subscribe(this);
    }
}