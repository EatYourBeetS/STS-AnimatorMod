package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.cards.animator.series.Overlord.Ainz;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCardBuilder;
import eatyourbeets.cards.base.AnimatorCard_Dynamic;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.resources.AnimatorResources_Strings;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.WeightedList;
import org.apache.logging.log4j.util.TriConsumer;
import patches.AbstractEnums;

import java.util.ArrayList;

public class AinzPower extends AnimatorPower
{
    private final static WeightedList<AnimatorCardBuilder> effectList = new WeightedList<>();

    public static final String POWER_ID = CreateFullID(AinzPower.class.getSimpleName());
    public static final int CHOICES = 4;

    public AinzPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.1f);
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.valueOf("3d0066")));
        GameActions.Bottom.SFX("ORB_DARK_EVOKE", 0.1f);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        for (int i = 0; i < this.amount; i++)
        {
            ChooseEffect();
        }

        this.flash();
    }

    private void ChooseEffect()
    {
        if (effectList.Count() == 0)
        {
            AinzEffect.GenerateAllEffects(effectList);
        }

        WeightedList<AnimatorCardBuilder> temp = new WeightedList<>(effectList);
        ArrayList<AnimatorCard_Dynamic> currentEffects = new ArrayList<>();

        for (int i = 0; i < CHOICES; i++)
        {
            currentEffects.add(temp.Retrieve(AbstractDungeon.cardRandomRng).Build());
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AnimatorCard_Dynamic card : currentEffects)
        {
            if (card != null)
            {
                card.applyPowers();
                group.addToTop(card);
            }
        }

        GameActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .SetMessage(CardRewardScreen.TEXT[1])
        .AddCallback(cards ->
        {
            if (!cards.isEmpty())
            {
                cards.get(0).use(AbstractDungeon.player, null);
            }
        });
    }

    private enum AinzEffect
    {
        PlayTopCard(22, 10, 1),
        ChannelRandomOrbs(23, 10, 2),
        GainTemporaryHP(15, 10, 8),
        ApplyBurning(19, 10, 4),
        ApplyPoison(18, 10, 6),
        DrawCards(14, 10, 2),
        GainThorns(9, 10, 3),
        DamageAll(5, 10, 12),
        GainIntellect(16, 8, 2),
        GainEnergy(17, 8, 2),
        GainAgility(8, 8, 2),
        GainForce(7, 8, 3),
        GainIntangibleLosePower(20, 4, 1),
        GainArtifactRemoveDebuffs(21, 4, 1);

        private static final CardStrings CARD_STRINGS = AnimatorResources.GetCardStrings(Ainz.ID);
        private static final String[] CARD_TEXT = AnimatorResources_Strings.SpecialEffects.TEXT;

        private int index;
        private int weight;
        private int number;

        AinzEffect(int index, int weight, int number)
        {
            this.index = index;
            this.weight = weight;
            this.number = number;
        }

        public static void GenerateAllEffects(WeightedList<AnimatorCardBuilder> list)
        {
            for (AinzEffect effect : AinzEffect.class.getEnumConstants())
            {
                list.Add(GenerateEffect(effect), effect.weight);
            }
        }

        public AnimatorCardBuilder Generate(TriConsumer<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
        {
            AnimatorCardBuilder builder = new AnimatorCardBuilder(Ainz.ID + "Alt");

            builder.SetText(CARD_STRINGS.NAME, CARD_TEXT[index], "");
            builder.SetProperties(AbstractCard.CardType.SKILL, AbstractEnums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL);
            builder.SetNumbers(number, number, number, number);
            builder.SetOnUse(onUseAction);

            return builder;
        }

        @SuppressWarnings("CodeBlock2Expr")
        protected static AnimatorCardBuilder GenerateEffect(AinzEffect effect)
        {
            switch (effect)
            {
                case PlayTopCard:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.Add(new PlayTopCardAction(m, false));
                    });
                }

                case ChannelRandomOrbs:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        for (int i = 0; i < c.magicNumber; i++)
                        {
                            GameActions.Bottom.ChannelRandomOrb(true);
                        }
                    });
                }

                case GainTemporaryHP:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.GainTemporaryHP(c.magicNumber);
                    });
                }

                case ApplyBurning:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
                        {
                            GameActions.Bottom.ApplyBurning(p, enemy, c.magicNumber);
                        }
                    });
                }

                case ApplyPoison:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
                        {
                            GameActions.Bottom.ApplyPoison(p, enemy, c.magicNumber);
                        }
                    });
                }

                case DrawCards:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.Draw(c.magicNumber);
                    });
                }

                case GainThorns:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.GainThorns(c.magicNumber);
                    });
                }

                case DamageAll:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
                        {
                            GameActions.Bottom.DealDamage(c, enemy, AbstractGameAction.AttackEffect.FIRE)
                            .SetPiercing(true, false);
                        }
                        GameUtilities.UsePenNib();
                    });
                }

                case GainIntellect:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.GainIntellect(c.magicNumber);
                    });
                }

                case GainEnergy:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.GainEnergy(c.magicNumber);
                    });
                }

                case GainAgility:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.GainAgility(c.magicNumber);
                    });
                }

                case GainForce:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.GainForce(c.magicNumber);
                    });
                }

                case GainIntangibleLosePower:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, c.magicNumber));
                        GameActions.Bottom.ReducePower(p, AinzPower.POWER_ID, 1);
                    });
                }

                case GainArtifactRemoveDebuffs:
                {
                    return effect.Generate((c, p, m) ->
                    {
                        GameActions.Bottom.Add(new RemoveDebuffsAction(p));
                        GameActions.Bottom.GainArtifact(c.magicNumber);
                    });
                }
            }

            return null;
        }
    }
}
