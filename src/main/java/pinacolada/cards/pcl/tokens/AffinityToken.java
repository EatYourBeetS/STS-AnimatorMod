package pinacolada.cards.pcl.tokens;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.interfaces.subscribers.OnTrySpendAffinitySubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public abstract class AffinityToken extends PCLCard implements OnTrySpendAffinitySubscriber
{
    public static final String ID = GR.PCL.CreateID(AffinityToken.class.getSimpleName());

    protected static final ArrayList<PCLCardData> cards = new ArrayList<>();
    protected final PCLAffinity affinity;

    protected static PCLCardData RegisterAffinityToken(Class<? extends PCLCard> type)
    {
        final PCLCardData data = Register(type).SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS);
        final CardStrings strings = GR.GetCardStrings(ID);
        data.Strings.DESCRIPTION = PCLJUtils.Format(strings.DESCRIPTION, data.Strings.EXTENDED_DESCRIPTION[0], data.Strings.EXTENDED_DESCRIPTION[1]);
        return data;
    }

    public static PCLAffinity GetAffinityFromCardID(String ID) {
        if (AffinityToken_Red.DATA.ID.equals(ID)) {
            return PCLAffinity.Red;
        }
        else if (AffinityToken_Green.DATA.ID.equals(ID)) {
            return PCLAffinity.Green;
        }
        else if (AffinityToken_Blue.DATA.ID.equals(ID)) {
            return PCLAffinity.Blue;
        }
        else if (AffinityToken_Orange.DATA.ID.equals(ID)) {
            return PCLAffinity.Orange;
        }
        else if (AffinityToken_Light.DATA.ID.equals(ID)) {
            return PCLAffinity.Light;
        }
        else if (AffinityToken_Dark.DATA.ID.equals(ID)) {
            return PCLAffinity.Dark;
        }
        else if (AffinityToken_Silver.DATA.ID.equals(ID)) {
            return PCLAffinity.Silver;
        }
        return PCLAffinity.Star;
    }

    public static ArrayList<PCLCardData> GetCards()
    {
        if (cards.isEmpty())
        {
            for (PCLAffinity affinity : PCLAffinity.Extended())
            {
                cards.add(GetCardData(affinity));
            }
        }

        return cards;
    }

    public static PCLCardData GetCardData(PCLAffinity affinity)
    {
        switch (affinity)
        {
            case Red: return AffinityToken_Red.DATA;
            case Green: return AffinityToken_Green.DATA;
            case Blue: return AffinityToken_Blue.DATA;
            case Orange: return AffinityToken_Orange.DATA;
            case Light: return AffinityToken_Light.DATA;
            case Dark: return AffinityToken_Dark.DATA;
            case Silver: return AffinityToken_Silver.DATA;
            case Star: return AffinityToken_Star.DATA;

            default:
            {
                throw new RuntimeException("Affinity token not supported for " + affinity);
            }
        }
    }

    public static AffinityToken GetCard(PCLAffinity affinity)
    {
        return (AffinityToken) GetCardData(affinity).CreateNewInstance();
    }

    public static AffinityToken GetCopy(PCLAffinity affinity, boolean upgraded)
    {
        return (AffinityToken) GetCardData(affinity).MakeCopy(upgraded);
    }

    protected AffinityToken(PCLCardData cardData, PCLAffinity affinity)
    {
        super(cardData);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);
        InitializeAffinity(affinity, 2, 0, 0);

        this.affinity = affinity;
        this.cropPortrait = false;
        this.portraitForeground = portraitImg;
        this.portraitImg = new AdvancedTexture(GR.GetTexture(GR.GetCardImage(ID), true), affinity.GetAlternateColor(0.55f));

        SetPurge(true);
        SetRetain(true);
    }

    public PCLAffinity GetAffinity() {
        return affinity;
    }

    public void OnUpgrade() {
        SetHarmonic(true);
        SetPermanentHaste(true);
    }

    public static SelectFromPile SelectTokenAction(String name, int amount)
    {
        return SelectTokenAction(name, amount, cards.size());
    }

    public static SelectFromPile SelectTokenAction(String name, int amount, int size)
    {
        return SelectTokenAction(name, amount, size, false);
    }

    public static SelectFromPile SelectTokenAction(String name, int amount, int size, boolean upgrade)
    {
        return new SelectFromPile(name, amount, CreateTokenGroup(size, PCLGameUtilities.GetRNG(), upgrade));
    }

    public static CardGroup CreateTokenGroup(int amount, Random rng, boolean upgrade)
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<PCLCardData> temp = new RandomizedList<>(GetCards());
        while (amount > 0 && temp.Size() > 0)
        {
            group.group.add(temp.Retrieve(rng, true).MakeCopy(upgrade));
            amount -= 1;
        }

        return group;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity)
                .SetAffinityChoices(this.affinity)
                .SetOptions(true, true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onTrySpendAffinity.Subscribe(this);
    }

    @Override
    public int OnTrySpendAffinity(PCLAffinity affinity, int amount, boolean isActuallySpending) {
        if (isActuallySpending && affinity.equals(this.affinity) && player.hand.contains(this)) {
            PCLCombatStats.onTrySpendAffinity.Unsubscribe(this);
            PCLGameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
            PCLActions.Last.Purge(this).ShowEffect(true);
            return 0;
        }
        return amount;
    }
}