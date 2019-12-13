package eatyourbeets.cards.animator.series.Konosuba;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnLoseHpSubscriber;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Eris extends AnimatorCard implements OnLoseHpSubscriber, OnBattleStartSubscriber
{
    public static final String ID = Register(Eris.class.getSimpleName(), EYBCardBadge.Special);

    public Eris()
    {
        this(true);
    }

    private Eris(boolean revive)
    {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 3);

        SetExhaust(true);
        SetHealing(true);
        SetSynergy(Synergies.Konosuba);

        if (revive && GameUtilities.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onLoseHp.Subscribe(this);
    }

    @Override
    public int OnLoseHp(int damageAmount)
    {
        if (!EffectHistory.HasActivatedLimited(cardID))
        {
            AbstractPlayer player = AbstractDungeon.player;
            if (InPlayerDeck() && damageAmount > 0 && player.currentHealth <= damageAmount)
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

                Eris temp = new Eris(false);
                if (upgraded)
                {
                    temp.upgrade();
                }

                GameEffects.List.Add(new ShowCardBrieflyEffect(temp));
                PlayerStatistics.onLoseHp.Unsubscribe(this);
                EffectHistory.TryActivateLimited(cardID);

                return 0;
            }
        }

        return damageAmount;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.Heal(magicNumber);
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
        if (GameUtilities.InBattle())
        {
            return player.hand.contains(this) || player.drawPile.contains(this) || player.discardPile.contains(this);
        }

        return false;
    }
}