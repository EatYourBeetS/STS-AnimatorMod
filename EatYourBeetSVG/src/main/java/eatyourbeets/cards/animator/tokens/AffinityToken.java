package eatyourbeets.cards.animator.tokens;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.base.*;
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

    protected static EYBCardData RegisterAffinityToken(Class<? extends AnimatorCard> type)
    {
        final EYBCardData data = Register(type).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
        final CardStrings strings = GR.GetCardStrings(ID);
        data.Strings.DESCRIPTION = JUtils.Format(strings.DESCRIPTION, data.Strings.DESCRIPTION);
        return data;
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

        Initialize(0, 2, 2, 4);
        SetUpgrade(0, 3, 0, 0);
        InitializeAffinity(affinity, 2, 0, 0);

        this.affinity = affinity;
        this.cropPortrait = false;
        this.portraitForeground = portraitImg;
        this.portraitImg = new ColoredTexture(GR.GetTexture(GR.GetCardImage(ID), true), affinity.GetAlternateColor(0.55f));

        SetPurge(true);
        SetRetainOnce(true);
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

        final EYBCardAffinities affinities = CombatStats.Affinities.GetHandAffinities(this);
        final int level = affinities.GetLevel(affinity, true);
        for (Affinity a : Affinity.Basic())
        {
            if (a != affinity && affinities.GetLevel(a, true) > level)
            {
                GameActions.Bottom.GainEnergy(1);
                GameActions.Bottom.SFX(SFX.RELIC_ACTIVATION, 0.75f, 0.85f);
            }
        }
    }
}