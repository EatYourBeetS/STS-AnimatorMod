package eatyourbeets.actions.animatorClassic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.animatorClassic.series.NoGameNoLife.Sora;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;

public class SoraAction extends EYBAction
{
    private static final String[] EFFECT_NAMES = Sora.DATA.Strings.EXTENDED_DESCRIPTION;
    private static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;
    private static final ArrayList<AnimatorClassicCardBuilder> attackPool = new ArrayList<>();
    private static final ArrayList<AnimatorClassicCardBuilder> defendPool = new ArrayList<>();
    private static final ArrayList<AnimatorClassicCardBuilder> preparePool = new ArrayList<>();

    private final RandomizedList<AnimatorClassicCardBuilder> attackList = new RandomizedList<>();
    private final RandomizedList<AnimatorClassicCardBuilder> defendList = new RandomizedList<>();
    private final RandomizedList<AnimatorClassicCardBuilder> prepareList = new RandomizedList<>();
    private final ArrayList<AnimatorClassicCardBuilder> currentEffects = new ArrayList<>();

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
        currentEffects.add(attackList.Retrieve(rng));
        currentEffects.add(defendList.Retrieve(rng));
        currentEffects.add(prepareList.Retrieve(rng));

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AnimatorClassicCardBuilder e : currentEffects)
        {
            AnimatorClassicCard card = e.Build();
            card.applyPowers();
            card.calculateCardDamage(null);
            group.addToTop(card);
        }

        GameActions.Top.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            AbstractCard card = cards.get(0);
            card.applyPowers();
            card.calculateCardDamage(null);
            card.use(player, null);

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

        if (attackList.Size() == 0)
        {
            attackList.AddAll(attackPool);
        }

        if (defendList.Size() == 0)
        {
            defendList.AddAll(defendPool);
        }

        if (prepareList.Size() == 0)
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
        DamageRandomTwice(0, "", 5),
        DamageAll(0, "", 6),
        GainForce(0, ACTIONS.GainAmount(2, GR.Tooltips.Force, true), 2),
        ApplyVulnerable(0, ACTIONS.ApplyToALL(2, GR.Tooltips.Vulnerable, true), 2),

        GainBlock(1, "", 7),
        GainAgility(1, ACTIONS.GainAmount(2, GR.Tooltips.Agility, true), 2),
        ApplyWeak(1, ACTIONS.ApplyToALL(2, GR.Tooltips.Weak, true), 2),
        GainTemporaryHP(1, ACTIONS.GainAmount(5, "{" + GR.Tooltips.TempHP + "}", true), 5),

        UpgradeAll(2, ACTIONS.UpgradeALLCardsInHand(true), 0),
        Motivate(2, ACTIONS.Motivate(1, true), 1),
        CycleCards(2, ACTIONS.Cycle(3, true), 3),
        GainIntellect(2, ACTIONS.GainAmount(2, GR.Tooltips.Intellect, true), 2);

        private int group;
        private String cardDescription;
        private String cardName;
        private int number;

        SoraEffect(int nameIndex, String description, int number)
        {
            this.group = nameIndex;
            this.cardDescription = description;
            this.cardName = EFFECT_NAMES[nameIndex];
            this.number = number;
        }

        public static void GenerateAllEffects()
        {
            for (SoraEffect effect : SoraEffect.class.getEnumConstants())
            {
                if (effect.group == 0)
                {
                    attackPool.add(GenerateEffect(effect));
                }
                else if (effect.group == 1)
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
        protected static AnimatorClassicCardBuilder GenerateEffect(SoraEffect effect)
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
                        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), c.magicNumber);
                    });
                }

                case ApplyVulnerable:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), c.magicNumber);
                    });
                }

                case GainIntellect:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.GainIntellect(c.magicNumber);
                    });
                }

                case CycleCards:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.Cycle(c.name, c.magicNumber).DrawInstantly(true);
                    });
                }

                case DamageAll:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        GameActions.Bottom.DealDamageToAll(c, AttackEffect.SMASH);
                        GameUtilities.RemoveDamagePowers();
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
                            GameActions.Bottom.DealDamageToRandomEnemy(c, AbstractGameAction.AttackEffect.SMASH);
                        }
                        GameUtilities.RemoveDamagePowers();
                    });
                }
            }

            return null;
        }

        protected AnimatorClassicCardBuilder GenerateInternal(ActionT3<EYBCard, AbstractPlayer, AbstractMonster> onUseAction)
        {
            AnimatorClassicCardBuilder builder = new AnimatorClassicCardBuilder(Sora.DATA.ID + "Alt");

            builder.SetText(cardName, cardDescription, "");
            builder.SetProperties(AbstractCard.CardType.SKILL, GR.Enums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL);
            builder.SetOnUse(onUseAction);

            if (this == GainBlock)
            {
                builder.SetNumbers(0, number, 0, 0);
            }
            else if (this == DamageAll)
            {
                builder.cardType = AbstractCard.CardType.ATTACK;
                builder.SetAttackType(EYBAttackType.Normal, EYBCardTarget.ALL);
                builder.SetNumbers(number, 0, 0, 0);
            }
            else if (this == DamageRandomTwice)
            {
                builder.cardType = AbstractCard.CardType.ATTACK;
                builder.SetAttackType(EYBAttackType.Normal, EYBCardTarget.Random, 2);
                builder.SetNumbers(number, 0, 2, 0);
            }
            else
            {
                builder.SetNumbers(0, 0, number, number);
            }

            return builder;
        }
    }
}
