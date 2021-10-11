package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.animator.special.Eris_Chris;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
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
            .PostInitialize(data -> data.AddPreview(new Eris_Chris(), false));

    public Eris()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 3);

        SetAffinity_Water(1);
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
                GameEffects.TopLevelList.ShowAndObtain(new Eris_Chris());
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
        GameActions.Bottom.RaiseLightLevel(1,upgraded);
        GameActions.Bottom.HealPlayerLimited(this, magicNumber);
    }

    private boolean CanRevive()
    {
        return GameUtilities.InBattle() && (player.hand.contains(this) || player.drawPile.contains(this) || player.discardPile.contains(this));
    }
}