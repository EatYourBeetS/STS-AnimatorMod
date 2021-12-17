package pinacolada.actions.special;

import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardBuilder;
import pinacolada.cards.pcl.series.NoGameNoLife.Sora;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class SoraAction extends EYBAction
{
    private static final String[] EFFECT_NAMES = Sora.DATA.Strings.EXTENDED_DESCRIPTION;
    private static final PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;
    private static final ArrayList<PCLCardBuilder> attackPool = new ArrayList<>();
    private static final ArrayList<PCLCardBuilder> defendPool = new ArrayList<>();
    private static final ArrayList<PCLCardBuilder> preparePool = new ArrayList<>();

    private final RandomizedList<PCLCardBuilder> attackList = new RandomizedList<>();
    private final RandomizedList<PCLCardBuilder> defendList = new RandomizedList<>();
    private final RandomizedList<PCLCardBuilder> prepareList = new RandomizedList<>();
    private final ArrayList<PCLCardBuilder> currentEffects = new ArrayList<>();

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
        for (PCLCardBuilder e : currentEffects)
        {
            PCLCard card = e.Build();
            card.applyPowers();
            card.calculateCardDamage(null);
            group.addToTop(card);
        }

        PCLActions.Top.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            AbstractCard card = cards.get(0);
            card.applyPowers();
            card.calculateCardDamage(null);
            card.use(player, null);

            if (amount > 1)
            {
                PCLActions.Top.Add(new SoraAction(name, this, amount - 1));
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
        GainForce(0, ACTIONS.GainAmount(2, GR.Tooltips.Might, true), 2),
        ApplyVulnerable(0, ACTIONS.ApplyToALL(2, GR.Tooltips.Vulnerable, true), 2),

        GainBlock(1, "", 7),
        GainAgility(1, ACTIONS.GainAmount(2, GR.Tooltips.Velocity, true), 2),
        ApplyWeak(1, ACTIONS.ApplyToALL(2, GR.Tooltips.Weak, true), 2),
        GainTemporaryHP(1, ACTIONS.GainAmount(5, "{" + GR.Tooltips.TempHP + "}", true), 5),

        UpgradeAll(2, ACTIONS.UpgradeALLCardsInHand(true), 0),
        Motivate(2, ACTIONS.Motivate(1, true), 1),
        CycleCards(2, ACTIONS.Cycle(3, true), 3),
        GainIntellect(2, ACTIONS.GainAmount(2, GR.Tooltips.Wisdom, true), 2);

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
        protected static PCLCardBuilder GenerateEffect(SoraEffect effect)
        {
            switch (effect)
            {
                case GainTemporaryHP:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.GainTemporaryHP(c.magicNumber);
                    });
                }

                case ApplyWeak:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), c.magicNumber);
                    });
                }

                case ApplyVulnerable:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), c.magicNumber);
                    });
                }

                case GainIntellect:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.GainWisdom(c.magicNumber);
                    });
                }

                case CycleCards:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.Cycle(c.name, c.magicNumber).DrawInstantly(true);
                    });
                }

                case DamageAll:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.DealCardDamageToAll(c, AttackEffect.SMASH);
                        PCLGameUtilities.UsePenNib();
                    });
                }

                case UpgradeAll:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.Add(new ArmamentsAction(true));
                        PCLActions.Bottom.Add(new RefreshHandLayout());
                    });
                }

                case Motivate:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.Motivate(c.magicNumber);
                    });
                }

                case GainAgility:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.GainVelocity(c.magicNumber);
                    });
                }

                case GainForce:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.GainMight(c.magicNumber);
                    });
                }

                case GainBlock:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        PCLActions.Bottom.GainBlock(c.block);
                    });
                }

                case DamageRandomTwice:
                {
                    return effect.GenerateInternal((c, p, m) ->
                    {
                        for (int i = 0; i < 2; i++)
                        {
                            PCLActions.Bottom.DealCardDamageToRandomEnemy(c, AttackEffects.BLUNT_LIGHT);
                        }
                        PCLGameUtilities.UsePenNib();
                    });
                }
            }

            return null;
        }

        protected PCLCardBuilder GenerateInternal(ActionT3<PCLCard, AbstractPlayer, AbstractMonster> onUseAction)
        {
            PCLCardBuilder builder = new PCLCardBuilder(Sora.DATA.ID + "Alt");

            builder.SetText(cardName, cardDescription, "");
            builder.SetProperties(AbstractCard.CardType.SKILL, GR.Enums.Cards.THE_FOOL, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL);
            builder.SetOnUse(onUseAction);

            if (this == GainBlock)
            {
                builder.SetNumbers(0, number, 0, 0, 1);
            }
            else if (this == DamageAll)
            {
                builder.cardType = AbstractCard.CardType.ATTACK;
                builder.SetAttackType(PCLAttackType.Normal, EYBCardTarget.ALL);
                builder.SetNumbers(number, 0, 0, 0, 1);
            }
            else if (this == DamageRandomTwice)
            {
                builder.cardType = AbstractCard.CardType.ATTACK;
                builder.SetAttackType(PCLAttackType.Normal, EYBCardTarget.Random, 2);
                builder.SetNumbers(number, 0, 2, 0, 1);
            }
            else
            {
                builder.SetNumbers(0, 0, number, number, 1);
            }

            return builder;
        }
    }
}
