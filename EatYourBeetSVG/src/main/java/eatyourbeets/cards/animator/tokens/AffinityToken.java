package eatyourbeets.cards.animator.tokens;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.animator.colorless.uncommon.AffinityToken_General;
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
        final EYBCardData data = Register(type).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
        final CardStrings strings = GR.GetCardStrings(ID);
        data.Strings.DESCRIPTION = JUtils.Format(strings.DESCRIPTION, data.Strings.EXTENDED_DESCRIPTION[0], data.Strings.EXTENDED_DESCRIPTION[1]);
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
            case Light: return AffinityToken_Light.DATA;
            case Dark: return AffinityToken_Dark.DATA;

            case Star:
            case General: return AffinityToken_General.DATA;

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

        Initialize(0, 5, 1, 2);
        SetUpgrade(0, 2, 0, 0);
        InitializeAffinity(affinity, 1, 1, 0);

        this.affinity = affinity;
        this.cropPortrait = false;
        this.portraitForeground = portraitImg;
        this.portraitImg = new AdvancedTexture(GR.GetTexture(GR.GetCardImage(ID), true), affinity.GetAlternateColor(0.55f));

        SetExhaust(true);
        SetRetain(true);
    }

    @Override
    public void triggerOnAffinitySeal(boolean manual)
    {
        GameActions.Last.Exhaust(this);
    }

    public static SelectFromPile SelectTokenAction(String name, boolean upgraded, boolean noBlock, int amount)
    {
        return SelectTokenAction(name, upgraded, noBlock, amount, cards.size());
    }

    public static SelectFromPile SelectTokenAction(String name, boolean upgraded, boolean noBlock, int amount, int size)
    {
        return new SelectFromPile(name, amount, CreateTokenGroup(size, upgraded, noBlock, GameUtilities.GetRNG()));
    }

    public static SelectFromPile SelectTokenAction(String name, boolean upgraded, boolean noBlock, int amount, Affinity... affinities)
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (Affinity a : affinities)
        {
            final EYBCard c = GetCopy(a, upgraded);
            if (noBlock)
            {
                GameUtilities.RemoveBlock(c);
            }

            group.group.add(c);
        }

        return new SelectFromPile(name, amount, group);
    }

    public static CardGroup CreateTokenGroup(int amount, boolean upgraded, boolean noBlock, Random rng)
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (amount == -1)
        {
            for (EYBCardData data : GetCards())
            {
                final EYBCard c = data.MakeCopy(upgraded);
                if (noBlock)
                {
                    GameUtilities.RemoveBlock(c);
                }

                group.group.add(c);
            }
        }
        else
        {
            final RandomizedList<EYBCardData> temp = new RandomizedList<>(GetCards());
            while (amount > 0 && temp.Size() > 0)
            {
                final EYBCard c = temp.Retrieve(rng, true).MakeCopy(upgraded);
                if (noBlock)
                {
                    GameUtilities.RemoveBlock(c);
                }

                group.group.add(c);
                amount -= 1;
            }
        }

        return group;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.RELIC_ACTIVATION, 0.75f, 0.85f);
        GameActions.Bottom.GainBlock(block).SetVFX(true, true);
        GameActions.Bottom.StackAffinityPower(affinity, magicNumber, false);
        CombatStats.Affinities.AddTempAffinity(affinity, secondaryValue);
    }
}