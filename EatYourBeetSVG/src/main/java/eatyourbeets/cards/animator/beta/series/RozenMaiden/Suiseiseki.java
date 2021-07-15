package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.tempCards.Insight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class Suiseiseki extends AnimatorCard
{
    private static final AbstractCard insight = new Insight();
    private static final AbstractCard slimed = new Slimed();

    public static final EYBCardData DATA = Register(Suiseiseki.class)
    		.SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public Suiseiseki()
    {
        super(DATA);

        Initialize(0, 4);
        SetUpgrade(0, 0);
        
        SetScaling(0, 1, 0);
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        if (upgraded)
        {
            SuiseisekiMove(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.Cycle(name, 1)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                if (IsStarter())
                {
                    SuiseisekiMove(cards.get(0));
                }
            }
            else
            {
                GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
            }
        });
    }

    public void SuiseisekiMove(AbstractCard card)
    {
        AbstractCard c;
        if (card.type == CardType.CURSE)
        {
            c = CreateRandomCurses.GetRandomCurse(rng);
        }
        else if (card.type == CardType.STATUS)
        {
            c = new Slimed();
        }
        else
            switch (card.rarity)
            {
                case RARE:
                    c = AbstractDungeon.getCard(CardRarity.RARE).makeCopy();
                    break;
                case UNCOMMON:
                    c = AbstractDungeon.getCard(CardRarity.UNCOMMON).makeCopy();
                    break;
                case COMMON:
                    c = AbstractDungeon.getCard(CardRarity.COMMON).makeCopy();
                    break;
                case CURSE:
                    c = CreateRandomCurses.GetRandomCurse(rng);
                    break;
                case BASIC:
                    c = MakeBasicCard();
                    break;
                case SPECIAL:
                    if (card instanceof AnimatorCard_UltraRare)
                        c = AbstractDungeon.getCard(CardRarity.RARE).makeCopy();
                    else
                        c = new Insight();
                    break;
                default:
                    c = new HigakiRinne();
                    break;
            }
        if (rng.randomBoolean(0.01f))
            c = new HigakiRinne();

        GameActions.Bottom.MakeCardInDiscardPile(c);

        GameActions.Bottom.GainAgility(1);
    }

    private static final RandomizedList<AbstractCard> OptionSet = new RandomizedList<>();
    private static CardColor currentcolor = null;

    public AbstractCard MakeBasicCard()
    {
        if (currentcolor != player.getCardColor())
        {
            OptionSet.Clear();
            currentcolor = player.getCardColor();
        }

        if (OptionSet.Size() == 0)
        {
            ArrayList<AbstractCard> AllCard = CardLibrary.getAllCards();
            for (AbstractCard c : AllCard)
                if (!c.hasTag(CardTags.HEALING))
                    if (!c.hasTag(GR.Enums.CardTags.TEMPORARY))
                        if (c.rarity == CardRarity.BASIC)
                            if (c.color == currentcolor || c.color == CardColor.COLORLESS)
                                OptionSet.Add(c);
        }

        return OptionSet.Retrieve(rng, false).makeCopy();
    }
}

// [A][Block] 4
// Cycle 1 card or exit your {Stance}.
// (On Discard or ){Starter}: Add a random card to your discard pile, 
// with rarity same as the card discarded, then gain [Agi].

