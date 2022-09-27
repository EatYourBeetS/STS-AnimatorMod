package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Wiz extends AnimatorCard
{
    public static final int POWER_COST = 3;
    public static final int CARD_CHOICE = 5;
    public static final EYBCardData DATA = Register(Wiz.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(GetClassCard(Apparition.ID, false), false));

    public Wiz()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetCostUpgrade(-1);

        SetAffinity_Blue(2);
        SetAffinity_Dark(1);

        SetPurge(true);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(CARD_CHOICE);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        int count = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c.uuid != uuid)
            {
                count += 1;
            }
        }

        SetUnplayable(count < 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.MakeCardInDrawPile(new Apparition());
                GameActions.Bottom.StackPower(new WizPower(player, 1));
            }
        });
    }

    public static class WizPower extends AnimatorClickablePower
    {
        public WizPower(AbstractCreature owner, int amount)
        {
            super(owner, Wiz.DATA, PowerTriggerConditionType.Affinity_Light, POWER_COST, __ -> player.exhaustPile.size() >= CARD_CHOICE, null);

            triggerCondition.SetUses(amount, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, POWER_COST, CARD_CHOICE);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            final RandomizedList<AbstractCard> group1 = new RandomizedList<>();
            final RandomizedList<AbstractCard> group2 = new RandomizedList<>();
            for (AbstractCard c : player.exhaustPile.group)
            {
                for (AbstractCard c2 : group1.GetInnerList())
                {
                    if (c2.cardID.equals(c.cardID))
                    {
                        group2.Add(c);
                        break;
                    }
                }

                group1.Add(c);
            }

            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            while (group.size() < CARD_CHOICE)
            {
                if (group1.Size() > 0)
                {
                    group.group.add(group1.Retrieve(rng));
                }
                else if (group2.Size() > 0)
                {
                    group.group.add(group1.Retrieve(rng));
                }
                else
                {
                    break;
                }
            }

            if (group.size() > 0)
            {
                GameActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false, false)
                .AddCallback(cards ->
                {
                   for (AbstractCard c : cards)
                   {
                       GameActions.Bottom.MoveCard(c, player.exhaustPile, player.hand);
                   }
                });
            }

            RemovePower(GameActions.Last);
        }
    }
}