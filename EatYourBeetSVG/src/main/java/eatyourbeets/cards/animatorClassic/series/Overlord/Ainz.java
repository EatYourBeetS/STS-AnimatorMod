package eatyourbeets.cards.animatorClassic.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public class Ainz extends AnimatorClassicCard
{
    private static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;
    private static final int CHOICES = 4;
    public static final EYBCardData DATA = Register(Ainz.class).SetPower(7, CardRarity.RARE);

    public Ainz()
    {
        super(DATA);

        Initialize(0, 0, CHOICES);
        SetCostUpgrade(-1);

        SetHealing(true);
        SetSeries(CardSeries.Overlord);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (this.cost > 0)
        {
            this.updateCost(-1);

            GameActions.Bottom.GainRandomAffinityPower(1, false);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new AinzPower(p, 1));
    }

    public static class AinzPower extends AnimatorClassicPower
    {
        private static final WeightedList<AnimatorClassicCardBuilder> effectList = new WeightedList<>();

        public AinzPower(AbstractPlayer owner, int amount)
        {
            super(owner, Ainz.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.9f, 1f);
            GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.valueOf("3d0066")));
            GameActions.Bottom.SFX("ORB_DARK_EVOKE", 0.9f, 1f);
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
            if (effectList.Size() == 0)
            {
                AinzEffect.GenerateAllEffects(effectList);
            }

            WeightedList<AnimatorClassicCardBuilder> temp = new WeightedList<>(effectList);
            ArrayList<AnimatorClassicCard_Dynamic> currentEffects = new ArrayList<>();

            for (int i = 0; i < CHOICES; i++)
            {
                currentEffects.add(temp.Retrieve(rng).Build());
            }

            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AnimatorClassicCard_Dynamic card : currentEffects)
            {
                if (card != null)
                {
                    card.calculateCardDamage(null);
                    group.addToTop(card);
                }
            }

            GameActions.Bottom.SelectFromPile(name, 1, group)
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                if (!cards.isEmpty())
                {
                    cards.get(0).use(player, null);
                }
            });
        }

        private enum AinzEffect
        {
            PlayTopCard(ACTIONS.PlayTopCard(true), 10, 1),
            ChannelRandomOrbs(ACTIONS.ChannelRandomOrbs(2, true), 10, 2),
            GainTemporaryHP(ACTIONS.GainAmount(8, "{" + GR.Tooltips.TempHP + "}", true), 10, 8),
            ApplyBurning(ACTIONS.ApplyToALL(4, GR.Tooltips.Burning, true), 10, 4),
            ApplyPoison(ACTIONS.ApplyToALL(6, GR.Tooltips.Poison, true), 10, 6),
            DrawCards(ACTIONS.Draw(2, true), 10, 2),
            GainThorns(ACTIONS.GainAmount(3, GR.Tooltips.Thorns, true), 10, 3),
            DamageAll("", 10, 12),
            GainIntellect(ACTIONS.GainAmount(2, GR.Tooltips.Intellect, true), 8, 2),
            GainEnergy(ACTIONS.GainAmount(2, GR.Tooltips.Energy, true), 8, 2),
            GainAgility(ACTIONS.GainAmount(2, GR.Tooltips.Agility, true), 8, 2),
            GainForce(ACTIONS.GainAmount(2, GR.Tooltips.Force, true), 8, 2);

            private final String text;
            private final int weight;
            private final int number;

            AinzEffect(String text, int weight, int number)
            {
                this.text = text;
                this.weight = weight;
                this.number = number;
            }

            public static void GenerateAllEffects(WeightedList<AnimatorClassicCardBuilder> list)
            {
                for (AinzEffect effect : AinzEffect.class.getEnumConstants())
                {
                    list.Add(GenerateEffect(effect), effect.weight);
                }
            }

            public AnimatorClassicCardBuilder Generate(ActionT3<EYBCard, AbstractPlayer, AbstractMonster> onUseAction)
            {
                AnimatorClassicCardBuilder builder = new AnimatorClassicCardBuilder(eatyourbeets.cards.animator.series.Overlord.Ainz.DATA.ID + "Alt");

                builder.SetText(Ainz.DATA.Strings.NAME, text, "");
                builder.SetProperties(AbstractCard.CardType.SKILL, GR.Animator.CardColor, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL);
                builder.SetOnUse(onUseAction);

                if (this == DamageAll)
                {
                    builder.SetNumbers(number, 0, 0, 0);
                    builder.SetAttackType(EYBAttackType.Elemental, EYBCardTarget.ALL);
                    builder.cardType = AbstractCard.CardType.ATTACK;
                }
                else
                {
                    builder.SetNumbers(0, 0, number, number);
                }

                return builder;
            }

            @SuppressWarnings("CodeBlock2Expr")
            private static AnimatorClassicCardBuilder GenerateEffect(AinzEffect effect)
            {
                switch (effect)
                {
                    case PlayTopCard:
                    {
                        return effect.Generate((c, p, m) ->
                        {
                            GameActions.Bottom.ReshuffleDiscardPile(true);
                            GameActions.Bottom.PlayCard(p.drawPile, m, CardGroup::getTopCard);
                        });
                    }

                    case ChannelRandomOrbs:
                    {
                        return effect.Generate((c, p, m) ->
                        {
                            GameActions.Bottom.ChannelRandomOrb(c.magicNumber);
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
                            GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), c.magicNumber);
                        });
                    }

                    case ApplyPoison:
                    {
                        return effect.Generate((c, p, m) ->
                        {
                            GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), c.magicNumber);
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
                            GameActions.Bottom.DealDamageToAll(c, AbstractGameAction.AttackEffect.FIRE);
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

//                case GainIntangibleLosePower:
//                {
//                    return effect.Generate((c, p, m) ->
//                    {
//                        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, c.magicNumber));
//                        GameActions.Bottom.ReducePower(p, AinzPower.POWER_ID, 1);
//                    });
//                }
//
//                case GainArtifactRemoveDebuffs:
//                {
//                    return effect.Generate((c, p, m) ->
//                    {
//                        GameActions.Bottom.Add(new RemoveDebuffsAction(p));
//                        GameActions.Bottom.GainArtifact(c.magicNumber);
//                    });
//                }
                }

                return null;
            }
        }
    }
}