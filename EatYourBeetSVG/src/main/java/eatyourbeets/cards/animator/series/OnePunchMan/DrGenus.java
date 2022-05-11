package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class DrGenus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DrGenus.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new DrGenus(1), false);
                data.AddPreview(new DrGenus(2), false);
                data.AddPreview(new DrGenus(3), false);
            });

    private final int effect;

    public DrGenus()
    {
        this(0);
    }

    private DrGenus(int effect)
    {
        super(DATA);

        Initialize(0, 0, 5, 3);

        if ((this.effect = effect) == 0)
        {
            SetAffinity_Blue(1);
            SetAffinity_Dark(1);

            SetPurge(true);
            SetEthereal(true);

            SetAffinityRequirement(Affinity.Blue, 3);
        }
        else
        {
            cardText.OverrideDescription(DATA.Strings.EXTENDED_DESCRIPTION[effect], true);
        }
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return effect > 0 ? null : super.GetCardPreview();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.TakeDamageAtEndOfTurn(magicNumber);
        GameActions.Bottom.FetchFromPile(name, 1, p.discardPile)
        .SetOptions(false, false)
        .SetFilter(c -> !c.hasTag(VOLATILE))
        .AddCallback(cards ->
        {
           for (AbstractCard c : cards)
           {
               if (info.CanActivateLimited && TryUseAffinity(Affinity.Blue) && CombatStats.TryActivateLimited(cardID))
               {
                   GameActions.Bottom.StackPower(new DrGenusPower(player, c, secondaryValue));
               }

               final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
               group.group.add(new DrGenus(1));
               group.group.add(new DrGenus(2));
               group.group.add(new DrGenus(3));
               GameActions.Bottom.SelectFromPile(name, 1, group)
               .SetOptions(false, false)
               .CancellableFromPlayer(false)
               .AddCallback(c, (selected, drGenus) -> ((DrGenus)drGenus.get(0)).ApplyEffect(selected));
           }
        });
    }

    protected void ApplyEffect(AbstractCard card)
    {
        switch (effect)
        {
            case 1:
                if (card.costForTurn >= 0)
                {
                    if (card.baseBlock > 0)
                    {
                        BlockModifiers.For(card).Add(cardID, card.baseBlock);
                    }
                    if (card.baseDamage > 0)
                    {
                        DamageModifiers.For(card).Add(cardID, card.baseDamage);
                    }
                    CostModifiers.For(card).Add(cardID, 1);
                    card.flash();
                }
                break;

            case 2:
                card.retain = false;
                card.selfRetain = false;
                card.isEthereal = true;
                if (card.costForTurn > 1)
                {
                    CostModifiers.For(card).Add(cardID, -1);
                }
                card.flash();
                break;

            case 3:
                if (card.baseBlock > 0)
                {
                    BlockModifiers.For(card).Add(cardID, rng.random(-2, 2));
                }
                if (card.baseDamage > 0)
                {
                    DamageModifiers.For(card).Add(cardID, rng.random(-2, 2));
                }
                GameActions.Bottom.SealAffinities(card, false, true);
                break;
        }
    }

    public static class DrGenusPower extends AnimatorPower
    {
        private final AbstractCard card;

        public DrGenusPower(AbstractCreature owner, AbstractCard card, int turns)
        {
            super(owner, DrGenus.DATA);

            this.card = card;
            this.ID += "_" + card.cardID;

            Initialize(turns, PowerType.BUFF, true);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, JUtils.ModifyString(card.name, w -> "#y" + w));
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            ReducePower(1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            GameActions.Bottom.MakeCardInHand(card.makeStatEquivalentCopy());
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new DrGenusPower(owner, card, amount);
        }
    }
}