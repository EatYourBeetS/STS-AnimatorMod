package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.animator.special.Eris_Chris;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.interfaces.subscribers.OnLoseHpSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Eris extends AnimatorCard implements OnLoseHpSubscriber
{
    public static final EYBCardData DATA = Register(Eris.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetMaxCopies(2)
            .PostInitialize(data -> data.AddPreview(new Eris_Chris(), true));

    public Eris()
    {
        super(DATA);

        Initialize(0, 0, 4, 3);
        SetUpgrade(0, 0, 3, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetExhaust(true);
        SetHealing(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int GetXValue() {
        return CombatStats.Affinities.GetAffinityLevel(Affinity.Light, true) / secondaryValue;
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
                Eris_Chris chris = new Eris_Chris();
                if (upgraded) {
                    chris.upgrade();
                }
                GameEffects.TopLevelList.ShowAndObtain(chris);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int val = GetXValue();
        if (val > 0) {
            GameActions.Bottom.GainInvocation(val);
        }
        GameActions.Bottom.HealPlayerLimited(this, magicNumber);
        TrySpendAffinity(Affinity.Light, CombatStats.Affinities.GetAffinityLevel(Affinity.Light, true));
    }

    private boolean CanRevive()
    {
        return GameUtilities.InBattle() && (player.hand.contains(this) || player.drawPile.contains(this) || player.discardPile.contains(this));
    }
}