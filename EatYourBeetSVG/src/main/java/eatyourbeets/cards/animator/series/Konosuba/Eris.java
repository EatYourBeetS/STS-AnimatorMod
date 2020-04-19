package eatyourbeets.cards.animator.series.Konosuba;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnLoseHpSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Eris extends AnimatorCard implements OnLoseHpSubscriber, OnBattleStartSubscriber
{
    public static final EYBCardData DATA = Register(Eris.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None);

    public Eris()
    {
        this(true);
    }

    private Eris(boolean revive)
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 3);

        SetExhaust(true);
        SetHealing(true);
        SetSynergy(Synergies.Konosuba);

        if (revive && CanSubscribeToEvents())
        {
            OnBattleStart();
        }
    }

    @Override
    public void OnBattleStart()
    {
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
            CombatStats.onLoseHp.Unsubscribe(this);
            CombatStats.TryActivateLimited(cardID);
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
        return GameUtilities.InBattle() && player.hand.contains(this) || player.drawPile.contains(this) || player.discardPile.contains(this);
    }
}