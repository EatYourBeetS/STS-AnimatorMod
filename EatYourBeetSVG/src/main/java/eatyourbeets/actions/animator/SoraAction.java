package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.animator.series.NoGameNoLife.Sora;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCardBuilder;
import eatyourbeets.resources.animator.AnimatorResources_Strings;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;
import org.apache.logging.log4j.util.TriConsumer;
import patches.AbstractEnums;

import java.util.ArrayList;

public class SoraAction extends EYBAction
{
    private static final ArrayList<AnimatorCardBuilder> attackPool = new ArrayList<>();
    private static final ArrayList<AnimatorCardBuilder> defendPool = new ArrayList<>();
    private static final ArrayList<AnimatorCardBuilder> preparePool = new ArrayList<>();

    private final RandomizedList<AnimatorCardBuilder> attackList = new RandomizedList<>();
    private final RandomizedList<AnimatorCardBuilder> defendList = new RandomizedList<>();
    private final RandomizedList<AnimatorCardBuilder> prepareList = new RandomizedList<>();
    private final ArrayList<AnimatorCardBuilder> currentEffects = new ArrayList<>();

    public SoraAction(String sourceName, int times)
    {
        this(sourceName, null, times);
    }

    protected SoraAction(String sourceName, SoraAction copy, int times)
    {
        super(ActionType.SPECIAL);

        InitializeRandomLists(copy);
        Initialize(times, sourceName);
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        currentEffects.clear();
        currentEffects.add(attackList.Retrieve(AbstractDungeon.cardRandomRng));
        currentEffects.add(defendList.Retrieve(AbstractDungeon.cardRandomRng));
        currentEffects.add(prepareList.Retrieve(AbstractDungeon.cardRandomRng));

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AnimatorCardBuilder e : currentEffects)
        {
            AnimatorCard card = e.Build();
            card.applyPowers();
            card.calculateCardDamage(null);
            group.addToTop(card);
        }

        GameActions.Top.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .SetMessage(CardRewardScreen.TEXT[1])
        .AddCallback(cards ->
        {
            AbstractCard card = cards.get(0);
            card.applyPowers();
            card.calculateCardDamage(null);
            card.use(AbstractDungeon.player, null);

            if (amount > 1)
            {
                GameActions.Top.Add(new SoraAction(name, this, amount - 1));
            }
        });
    }

    private void InitializeRandomLists(SoraAction copy)
    {
        if (copy != null)
        {
            attackList.AddAll(copy.attackList.GetInnerList());
            defendList.AddAll(copy.defendList.GetInnerList());
            prepareList.AddAll(copy.prepareList.GetInnerList());
        }

        if (attackList.Count() == 0)
        {
            attackList.AddAll(attackPool);
        }

        if (defendList.Count() == 0)
        {
            defendList.AddAll(defendPool);
        }

        if (prepareList.Count() == 0)
        {
            prepareList.AddAll(preparePool);
        }
    }

    static
    {
        SoraEffect.GenerateAllEffects();
    }

    private enum SoraEffect
    {
        DamageRandomTwice(0, 4, 5),
        DamageAll(0, 5, 6),
        GainForce(0, 7, 2),
        ApplyVulnerable(0, 12, 2),

        GainBlock(1, 6, 7),
        GainAgility(1, 8, 2),
        ApplyWeak(1, 11, 2),
        GainTemporaryHP(1, 15, 5),

        UpgradeAll(2, 3, 0),
        Motivate(2, 10, 1),
        CycleCards(2, 13, 3),
        DrawCards(2, 14, 2);

        private static final String[] CARD_TEXT = AnimatorResources_Strings.SpecialEffects.TEXT;

        private int descriptionIndex;
        private int nameIndex;
        private int number;

        SoraEffect(int nameIndex, int descriptionIndex, int number)
        {
            this.descriptionIndex = descriptionIndex;
            this.nameIndex = nameIndex;
            this.number = number;
        }

        public static void GenerateAllEffects()
        {
            for (SoraEffect effect : SoraEffect.class.getEnumConstants())
            {
                if (effect.nameIndex == 0)
                {
                    attackPool.add(GenerateEffect(effect));
                }
                else if (effect.nameIndex == 1)
                {
                    defendPool.add(GenerateEffect(effect));
                }
                else
                {
                    preparePool.add(GenerateEffect(effect));
                }
            }
        }

        @SuppressWarnings("CodeBlock2Expr")
        protected static AnimatorCardBuilder GenerateEffect(SoraEffect effect)
        {
            switch (effect)
            {
                case GainTemporaryHP:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.GainTemporaryHP(c.magicNumber);
                    });
                }

                case ApplyWeak:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
                        {
                            GameActions.Bottom.ApplyWeak(p, enemy, c.magicNumber);
                        }
                    });
                }

                case ApplyVulnerable:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
                        {
                            GameActions.Bottom.ApplyVulnerable(p, enemy, c.magicNumber);
                        }
                    });
                }

                case DrawCards:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.Draw(c.magicNumber);
                    });
                }

                case CycleCards:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.Cycle(c.name, c.magicNumber);
                    });
                }

                case DamageAll:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.DealDamageToAll(c, AttackEffect.SMASH)
                        .SetPiercing(true, false);
                        GameUtilities.UsePenNib();
                    });
                }

                case UpgradeAll:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.Add(new ArmamentsAction(true));
                        GameActions.Bottom.Add(new RefreshHandLayout());
                    });
                }

                case Motivate:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.Motivate(c.magicNumber);
                    });
                }

                case GainAgility:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.GainAgility(c.magicNumber);
                    });
                }

                case GainForce:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.GainForce(c.magicNumber);
                    });
                }

                case GainBlock:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.GainBlock(c.block);
                    });
                }

                case DamageRandomTwice:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        for (int i = 0; i < 2; i++)
                        {
                            GameActions.Bottom.DealDamageToRandomEnemy(c, AbstractGameAction.AttackEffect.SMASH)
                            .SetPiercing(true, false);
                        }
                        GameUtilities.UsePenNib();
                    });
                }
            }

            return null;
        }

        protected AnimatorCardBuilder GenerateInternal(TriConsumer<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
        {
            AnimatorCardBuilder builder = new AnimatorCardBuilder(Sora.ID + "Alt");

            builder.SetText(CARD_TEXT[nameIndex], CARD_TEXT[descriptionIndex], "");
            builder.SetProperties(AbstractCard.CardType.SKILL, AbstractEnums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL);
            builder.SetNumbers(number, number, number, number);
            builder.SetOnUse(onUseAction);

            if (this == SoraEffect.DamageAll)
            {
                builder.isMultiDamage = true;
            }
            else if (this == SoraEffect.DamageRandomTwice)
            {
                builder.magicNumber = 2;
            }

            return builder;
        }
    }
}
