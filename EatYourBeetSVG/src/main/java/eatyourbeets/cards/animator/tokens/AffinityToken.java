package eatyourbeets.cards.animator.tokens;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnTrySpendAffinitySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public abstract class AffinityToken extends AnimatorCard implements OnTrySpendAffinitySubscriber
{
    public static final String ID = GR.Animator.CreateID(AffinityToken.class.getSimpleName());

    protected static final ArrayList<EYBCardData> cards = new ArrayList<>();
    protected final Affinity affinity;

    protected static EYBCardData RegisterAffinityToken(Class<? extends AnimatorCard> type)
    {
        final EYBCardData data = Register(type).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
        final CardStrings strings = GR.GetCardStrings(ID);
        data.Strings.DESCRIPTION = JUtils.Format(strings.DESCRIPTION, data.Strings.EXTENDED_DESCRIPTION[0], data.Strings.EXTENDED_DESCRIPTION[1]);
        return data;
    }

    public static Affinity GetAffinityFromCardID(String ID) {
        if (AffinityToken_Red.DATA.ID.equals(ID)) {
            return Affinity.Red;
        }
        else if (AffinityToken_Green.DATA.ID.equals(ID)) {
            return Affinity.Green;
        }
        else if (AffinityToken_Blue.DATA.ID.equals(ID)) {
            return Affinity.Blue;
        }
        else if (AffinityToken_Orange.DATA.ID.equals(ID)) {
            return Affinity.Orange;
        }
        else if (AffinityToken_Light.DATA.ID.equals(ID)) {
            return Affinity.Light;
        }
        else if (AffinityToken_Dark.DATA.ID.equals(ID)) {
            return Affinity.Dark;
        }
        else if (AffinityToken_Silver.DATA.ID.equals(ID)) {
            return Affinity.Silver;
        }
        return Affinity.Star;
    }

    public static ArrayList<EYBCardData> GetCards()
    {
        if (cards.isEmpty())
        {
            for (Affinity affinity : Affinity.Extended())
            {
                cards.add(GetCardData(affinity));
            }
        }

        return cards;
    }

    public static EYBCardData GetCardData(Affinity affinity)
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
            //case Star: return AffinityToken_Star.DATA;

            default:
            {
                throw new RuntimeException("Affinity token not supported for " + affinity);
            }
        }
    }

    public static AffinityToken GetCard(Affinity affinity)
    {
        return (AffinityToken) GetCardData(affinity).CreateNewInstance();
    }

    public static AffinityToken GetCopy(Affinity affinity, boolean upgraded)
    {
        return (AffinityToken) GetCardData(affinity).MakeCopy(upgraded);
    }

    protected AffinityToken(EYBCardData cardData, Affinity affinity)
    {
        super(cardData);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);
        InitializeAffinity(affinity, 2, 0, 0);

        this.affinity = affinity;
        this.cropPortrait = false;
        this.portraitForeground = portraitImg;
        this.portraitImg = new AdvancedTexture(GR.GetTexture(GR.GetCardImage(ID), true), affinity.GetAlternateColor(0.55f));

        SetRetain(true);
    }

    public void OnUpgrade() {
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
        return new SelectFromPile(name, amount, CreateTokenGroup(size, GameUtilities.GetRNG(), upgrade));
    }

    public static CardGroup CreateTokenGroup(int amount, Random rng, boolean upgrade)
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<EYBCardData> temp = new RandomizedList<>(GetCards());
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
        GameActions.Bottom.AddAffinity(this.affinity, magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onTrySpendAffinity.Subscribe(this);
    }

    @Override
    public int OnTrySpendAffinity(Affinity affinity, int amount, boolean canUseStar, boolean isActuallySpending) {
        if (isActuallySpending && affinity.equals(this.affinity) && player.hand.contains(this)) {
            CombatStats.onTrySpendAffinity.Unsubscribe(this);
            GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
            GameActions.Last.Purge(this).ShowEffect(true);
            return 0;
        }
        return amount;
    }
}