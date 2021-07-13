package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.interfaces.subscribers.OnLoseHpSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Eris extends AnimatorCard implements OnLoseHpSubscriber
{
    public static final EYBCardData DATA = Register(Eris.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None);

    public Eris()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 3);

        SetExhaust(true);
        SetHealing(true);
        SetSeries(CardSeries.Konosuba);
        SetAffinity(0, 0, 1, 2, 0);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onLoseHp.Subscribe(this);
    }

    @Override
    public int OnLoseHp(int damageAmount)
    {
        if (CombatStats.HasActivatedLimited(cardID))
        {
            CombatStats.onLoseHp.Unsubscribe(this);
            return damageAmount;
        }

        if (damageAmount > 0 && player.currentHealth <= damageAmount && CanRevive())
        {
            AbstractCard c = GameUtilities.GetMasterDeckInstance(uuid);
            if (c != null && GameUtilities.CanRemoveFromDeck(c))
            {
                player.masterDeck.removeCard(c);
            }

            for (AbstractCard card : GameUtilities.GetAllInBattleInstances(uuid))
            {
                player.discardPile.removeCard(card);
                player.drawPile.removeCard(card);
                player.hand.removeCard(card);
            }

            CombatStats.TryActivateLimited(cardID);
            CombatStats.onLoseHp.Unsubscribe(this);
            GameEffects.List.Add(new ShowCardBrieflyEffect(makeStatEquivalentCopy()));
            GameUtilities.RefreshHandLayout();
            return 0;
        }

        return damageAmount;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Heal(magicNumber);
    }

    private boolean CanRevive()
    {
        return GameUtilities.InBattle() && (player.hand.contains(this) || player.drawPile.contains(this) || player.discardPile.contains(this));
    }
}