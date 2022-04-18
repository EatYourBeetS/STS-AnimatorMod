package eatyourbeets.cards.unnamed.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.cards.base.modifiers.PersistentCardModifiers;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

public class RuneCarving extends UnnamedCard
{
    public static final EYBCardData DATA = Register(RuneCarving.class)
            .SetMaxCopies(1)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    private Random instanceRNG;

    public RuneCarving()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1);

        SetObtainableInCombat(false);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        if (player.masterDeck.group.size() < magicNumber || !CombatStats.TryActivateLimited(cardID))
        {
            return;
        }

        final WeightedList<AbstractCard> choice1 = new WeightedList<>();
        final WeightedList<AbstractCard> choice2 = new WeightedList<>();
        final RandomizedList<AbstractCard> deck = new RandomizedList<>(player.masterDeck.group);
        while (deck.Size() > 0)
        {
            final AbstractCard c = deck.Retrieve(instanceRNG);
            if (!GameUtilities.IsHindrance(c) && (c.baseBlock > 0 || c.baseDamage > 0))
            {
                int weight;
                switch (c.rarity)
                {
                    case BASIC: weight = 2; break;
                    case COMMON: weight = 2; break;
                    case UNCOMMON: weight = 2; break;
                    case RARE: weight = 1; break;
                    default: weight = 1; break;
                }

                boolean first = true;
                for (AbstractCard c2 : choice1.GetInnerList())
                {
                    if (c2.cardID.equals(c.cardID))
                    {
                        first = false;
                        break;
                    }
                }

                if (first)
                {
                    choice1.Add(c, weight);
                }
                else
                {
                    choice2.Add(c, weight);
                }
            }
        }

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < magicNumber)
        {
            if (choice1.Size() > 0)
            {
                group.group.add(choice1.Retrieve(instanceRNG));
            }
            else
            {
                group.group.add(choice2.Retrieve(instanceRNG));
            }
        }

        GameActions.Bottom.SelectFromPile(name, 1, group)
        .CancellableFromPlayer(false)
        .AddCallback(cards ->
        {
            final int bonus = secondaryValue + (GR.Common.Dungeon.IsUnnamedReign() ? 1 : 0);
            for (AbstractCard card : cards)
            {
                PersistentCardModifiers.Apply(card, bonus, bonus, true);

                final float x = Settings.WIDTH * 0.4f;
                final float y = Settings.HEIGHT * 0.5f;
                GameEffects.TopLevelQueue.ShowCardBriefly(card.makeStatEquivalentCopy(), x, y);
                GameEffects.TopLevelQueue.Add(new UpgradeShineEffect(x, y));
            }
        });
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        instanceRNG = GameUtilities.GenerateNewRNG(13, 17);
    }
}