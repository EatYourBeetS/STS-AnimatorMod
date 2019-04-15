package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnBattleStartSubscriber;
import eatyourbeets.subscribers.OnLoseHpSubscriber;

public class Eris extends AnimatorCard implements OnLoseHpSubscriber, OnBattleStartSubscriber
{
    public static final String ID = CreateFullID(Eris.class.getSimpleName());

    public Eris()
    {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 4);

        this.exhaust = true;
        this.tags.add(CardTags.HEALING);

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onLoseHp.Subscribe(this);
    }

    @Override
    public int OnLoseHp(int damageAmount)
    {
        AbstractPlayer player = AbstractDungeon.player;
        if (InPlayerDeck() && damageAmount > 0 && player.currentHealth < damageAmount)
        {
            AbstractCard c = StSLib.getMasterDeckEquivalent(this);
            if (c != null)
            {
                player.masterDeck.removeCard(c);
            }
            for (AbstractCard card : GetAllInBattleInstances.get(this.uuid))
            {
                player.discardPile.removeCard(card);
                player.drawPile.removeCard(card);
                player.hand.removeCard(card);
                player.hand.refreshHandLayout();
            }

            Eris temp = new Eris();
            if (upgraded)
            {
                temp.upgrade();
            }
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(temp));
            PlayerStatistics.onLoseHp.Unsubscribe(this);

            return 0;
        }

        return damageAmount;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(3);
        }
    }

    private boolean InPlayerDeck()
    {
        AbstractPlayer player = AbstractDungeon.player;
        if (PlayerStatistics.InBattle())
        {
            return player.hand.contains(this) || player.drawPile.contains(this) || player.discardPile.contains(this);
        }

        return false;
    }
}