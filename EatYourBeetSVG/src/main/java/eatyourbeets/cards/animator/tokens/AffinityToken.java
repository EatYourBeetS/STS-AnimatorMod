package eatyourbeets.cards.animator.tokens;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public abstract class AffinityToken extends AnimatorCard
{
    public static final String ID = GR.Animator.CreateID(AffinityToken.class.getSimpleName());

    protected static final ArrayList<EYBCardData> cards = new ArrayList<>();
    protected final Affinity affinity;

    public static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    }

    public static ArrayList<EYBCardData> GetCards()
    {
        if (cards.isEmpty())
        {
            for (Affinity affinity : Affinity.Basic())
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

        Initialize(0, 4, 1, 4);
        SetUpgrade(0, 0, 1, 0);
        InitializeAffinity(affinity, 2, 0, 0);

        this.affinity = affinity;
        this.cropPortrait = false;
        this.portraitForeground = portraitImg;
        this.portraitImg = new ColoredTexture(GR.GetTexture(GR.GetCardImage(ID), true), affinity.GetAlternateColor(0.55f));

        SetPurge(true);
        SetRetainOnce(true);

        if (affinity != Affinity.General)
        {
            SetAffinityRequirement(GetAffinityRequirement1(), secondaryValue);
            SetAffinityRequirement(GetAffinityRequirement2(), secondaryValue);
        }
    }

    @Override
    protected String GetRawDescription()
    {
        final Affinity a1 = GetAffinityRequirement1();
        final Affinity a2 = GetAffinityRequirement2();
        return a1 == null ? super.GetRawDescription() : super.GetRawDescription(a1.GetTooltip().id, a2.GetTooltip().id);
    }

    public static SelectFromPile SelectTokenAction(String name, int amount, int size)
    {
        return new SelectFromPile(name, amount, CreateTokenGroup(size, GameUtilities.GetRNG()));
    }

    public static CardGroup CreateTokenGroup(int amount, Random rng)
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<EYBCardData> temp = new RandomizedList<>(GetCards());
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
        GameActions.Bottom.GainBlock(block);
        CombatStats.Affinities.BonusAffinities.Add(affinity, magicNumber);

        if (CheckAffinity(GetAffinityRequirement1()) || CheckAffinity(GetAffinityRequirement2()))
        {
            GameActions.Bottom.GainEnergy(1);
            GameActions.Bottom.SFX(SFX.RELIC_ACTIVATION, 0.75f, 0.85f);
        }
    }

    protected abstract Affinity GetAffinityRequirement1();
    protected abstract Affinity GetAffinityRequirement2();
}