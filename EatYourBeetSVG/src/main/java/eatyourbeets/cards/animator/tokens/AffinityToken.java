package eatyourbeets.cards.animator.tokens;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public abstract class AffinityToken extends AnimatorCard
{
    public static final String ID = GR.Animator.CreateID(AffinityToken.class.getSimpleName());

    protected static final ArrayList<EYBCardData> cards = new ArrayList<>();
    protected final AffinityType affinityType;

    public static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    }

    public static ArrayList<EYBCardData> GetCards()
    {
        if (cards.isEmpty())
        {
            for (AffinityType type : AffinityType.BasicTypes())
            {
                cards.add(GetCard(type));
            }
        }

        return cards;
    }

    public static EYBCardData GetCard(AffinityType type)
    {
        switch (type)
        {
            case Red: return AffinityToken_Red.DATA;
            case Green: return AffinityToken_Green.DATA;
            case Blue: return AffinityToken_Blue.DATA;
            case Orange: return AffinityToken_Orange.DATA;
            case Light: return AffinityToken_Light.DATA;
            case Dark: return AffinityToken_Dark.DATA;
            //case Star: return AffinityToken_Star.DATA;

            default:
            {
                throw new RuntimeException("Affinity token not supported for " + type);
            }
        }
    }

    public static AffinityToken GetCopy(AffinityType type, boolean upgraded)
    {
        return (AffinityToken) GetCard(type).MakeCopy(upgraded);
    }

    protected AffinityToken(EYBCardData cardData, AffinityType affinityType)
    {
        super(cardData);

        Initialize(0, 0, 0, 3);
        SetCostUpgrade(-1);
        InitializeAffinity(affinityType, 2, 0, 0);

        this.affinityType = affinityType;
        this.cropPortrait = false;
        this.portraitForeground = portraitImg;
        this.portraitImg = new ColoredTexture(GR.GetTexture(GR.GetCardImage(ID), true), affinityType.GetAlternateColor(0.55f));

        SetPurge(true);
        SetRetainOnce(true);
    }

    public static SelectFromPile SelectTokenAction(String name, int amount, int size)
    {
        return new SelectFromPile(name, amount, CreateTokenGroup(size, GameUtilities.GetRNG()));
    }

    public static CardGroup CreateTokenGroup(int amount, Random rng)
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        RandomizedList<EYBCardData> temp = new RandomizedList<>(GetCards());

        while (amount > 0 && temp.Size() > 0)
        {
            group.group.add(temp.Retrieve(rng, true).MakeCopy(false));
            amount -= 1;
        }

        return group;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        CombatStats.Affinities.BonusAffinities.Add(affinityType, 1);
    }
}