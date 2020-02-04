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
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnLoseHpSubscriber;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Eris extends AnimatorCard implements OnLoseHpSubscriber, OnBattleStartSubscriber
{
    public static final String ID = Register_Old(Eris.class);

    public Eris()
    {
        this(true);
    }

    private Eris(boolean revive)
    {
        super(ID, 0, CardRarity.RARE, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 3);

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
        if (EffectHistory.HasActivatedLimited(cardID))
        {
            PlayerStatistics.onLoseHp.Unsubscribe(this);
            return damageAmount;
        }

        AbstractPlayer player = AbstractDungeon.player;
        if (damageAmount > 0 && player.currentHealth <= damageAmount && CanRevive())
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

        return damageAmount;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Heal(magicNumber);
    }

    private boolean CanRevive()
    {
        if (GameUtilities.InBattle())
        {
            AbstractPlayer player = AbstractDungeon.player;
            return player.hand.contains(this) || player.drawPile.contains(this) || player.discardPile.contains(this);
        }

        return false;
    }
}