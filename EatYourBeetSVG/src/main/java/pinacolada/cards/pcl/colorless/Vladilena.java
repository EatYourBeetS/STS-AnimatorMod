package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.RotatingList;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Vladilena extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Vladilena.class)
            .SetSkill(1, CardRarity.RARE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.EightySix)
            .PostInitialize(data ->
            {
                for (PCLCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), true);
                }
            });

    public Vladilena()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0,0,1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Silver(1);

        SetDrawPileCardPreview(this::FindCards);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<AbstractCard> pile = new RandomizedList<>(player.drawPile.group);
        pile.AddAll(player.discardPile.group);
        while (group.size() < magicNumber && pile.Size() > 0)
        {
            AbstractCard ca = pile.Retrieve(rng);
            if (ca != null && ca.type == CardType.ATTACK) {
                group.addToTop(ca);
            }
        }

        if (group.size() >= 1) {
            PCLActions.Bottom.ExhaustFromPile(name, 1, group)
                    .SetOptions(false, false)
                    .SetFilter(c -> c.type == CardType.ATTACK)
                    .AddCallback(cards -> {
                        for (AbstractCard c : cards) {
                            PCLActions.Top.PlayCopy(c.makeStatEquivalentCopy(), m);
                            PCLActions.Top.PlayCopy(c.makeStatEquivalentCopy(), m).AddCallback(() -> {
                                if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                                    PCLActions.Bottom.Add(AffinityToken.SelectTokenAction(name, 1, magicNumber, upgraded)
                                            .AddCallback(tokens ->
                                            {
                                                boolean isLoyal = (c.rarity.equals(CardRarity.BASIC) || c.costForTurn == 0) && info.TryActivateLimited();
                                                for (AbstractCard t : tokens)
                                                {
                                                    if (isLoyal) {
                                                        PCLActions.Top.ModifyTag(t, LOYAL, true);
                                                    }
                                                    PCLActions.Bottom.MakeCardInHand(t);
                                                }
                                            }));
                                }
                            });
                            PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, c.costForTurn * 2, AttackEffects.SLASH_VERTICAL);

                        }
                    });
        }

    }

    protected void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        cards.Clear();
        for (AbstractCard c : player.drawPile.group)
        {
            if (IsValid(c, target))
            {
                cards.Add(c);
            }
        }
        for (AbstractCard c : player.discardPile.group)
        {
            if (IsValid(c, target))
            {
                cards.Add(c);
            }
        }
    }

    protected boolean IsValid(AbstractCard c, AbstractMonster target) {
        return c.type == CardType.ATTACK && PCLGameUtilities.IsPlayable(c, target) && !c.tags.contains(GR.Enums.CardTags.VOLATILE);
    }
}