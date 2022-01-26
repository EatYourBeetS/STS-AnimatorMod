package pinacolada.cards.pcl.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.interfaces.subscribers.OnLosingHPSubscriber;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.cards.pcl.special.Eris_Chris;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Eris extends PCLCard implements OnLosingHPSubscriber
{
    public static final PCLCardData DATA = Register(Eris.class)
            .SetSkill(0, CardRarity.RARE, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetMaxCopies(2)
            .PostInitialize(data -> data.AddPreview(new Eris_Chris(), true));

    public Eris()
    {
        super(DATA);

        Initialize(0, 0, 4, 2);
        SetUpgrade(0, 0, 3, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetPurge(true);
        SetHealing(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int GetXValue() {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Light, true) / secondaryValue;
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onLosingHP.Subscribe(this);
    }

    @Override
    public int OnLosingHP(int damageAmount)
    {
        if (CombatStats.HasActivatedLimited(cardID))
        {
            PCLCombatStats.onLosingHP.Unsubscribe(this);
            return damageAmount;
        }

        if (damageAmount > 0 && player.currentHealth <= damageAmount && CanRevive())
        {
            AbstractCard c = PCLGameUtilities.GetMasterDeckInstance(uuid);
            if (c != null && PCLGameUtilities.CanRemoveFromDeck(c))
            {
                player.masterDeck.removeCard(c);
                Eris_Chris chris = new Eris_Chris();
                if (upgraded) {
                    chris.upgrade();
                }
                PCLGameEffects.TopLevelList.ShowAndObtain(chris);
            }

            for (AbstractCard card : PCLGameUtilities.GetAllInBattleInstances(uuid))
            {
                player.discardPile.removeCard(card);
                player.drawPile.removeCard(card);
                player.hand.removeCard(card);
            }

            CombatStats.TryActivateLimited(cardID);
            PCLCombatStats.onLosingHP.Unsubscribe(this);
            PCLGameEffects.List.Add(new ShowCardBrieflyEffect(makeStatEquivalentCopy()));
            PCLGameUtilities.RefreshHandLayout();
            return 0;
        }

        return damageAmount;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int val = GetXValue();
        if (val > 0) {
            PCLActions.Bottom.GainInvocation(val);
        }
        PCLActions.Bottom.Heal(magicNumber);
        TrySpendAffinity(PCLAffinity.Light, PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Light, true));
    }

    private boolean CanRevive()
    {
        return PCLGameUtilities.InBattle() && (player.hand.contains(this) || player.drawPile.contains(this) || player.discardPile.contains(this));
    }
}