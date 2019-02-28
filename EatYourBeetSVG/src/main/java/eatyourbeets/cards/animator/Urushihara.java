package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.UrushiharaLazyAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnBattleStartSubscriber;
import eatyourbeets.subscribers.OnAfterCardDrawnSubscriber;

public class Urushihara extends AnimatorCard implements OnBattleStartSubscriber, OnAfterCardDrawnSubscriber
{
    public static final String ID = CreateFullID(Urushihara.class.getSimpleName());

    public Urushihara()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(9,0);

        AddExtendedDescription();
        this.isMultiDamage = true;

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            PlayerStatistics.onAfterCardDrawn.Subscribe(this);
        }

        SetSynergy(Synergies.HatarakuMaouSama);
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onAfterCardDrawn.Subscribe(this);
    }

    @Override
    public void OnAfterCardDrawn(AbstractCard card)
    {
        if (card == this && AbstractDungeon.miscRng.randomBoolean())
        {
            GameActionsHelper.AddToBottom(new UrushiharaLazyAction(this));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.ChannelOrb(new Dark(), true);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}