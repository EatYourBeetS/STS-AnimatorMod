package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class ZorzalElCaesar extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ZorzalElCaesar.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    private static final int ENERGY_COST = 1;

    public ZorzalElCaesar()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Orange(1);

        SetDelayed(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ZorzalElCaesarPower(p, secondaryValue));
    }

    public static class ZorzalElCaesarPower extends AnimatorClickablePower
    {
        public static final int DRAW_REDUCTION = 1;

        public ZorzalElCaesarPower(AbstractCreature owner, int amount)
        {
            super(owner, ZorzalElCaesar.DATA, PowerTriggerConditionType.Energy, ZorzalElCaesar.ENERGY_COST);

            triggerCondition.SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, DRAW_REDUCTION);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            GameUtilities.ModifyCardDrawPerTurn(-DRAW_REDUCTION, 1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            GameUtilities.ModifyCardDrawPerTurn(DRAW_REDUCTION, 1);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.SelectFromPile(name, Integer.MAX_VALUE, player.exhaustPile, player.drawPile, player.discardPile, player.hand)
            .SetOptions(false, false)
            .SetFilter(GameUtilities::HasRedAffinity)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    ((EYBCard)c).SetHaste(true);
                }
            });

            flashWithoutSound();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            final RandomizedList<AbstractCard> pool = GameUtilities.GetCardPoolInCombat(CardRarity.UNCOMMON);
            final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            while (choice.size() < 3 && pool.Size() > 0)
            {
                AbstractCard temp = pool.Retrieve(rng);
                if (temp.cost == 0 && !(temp.cardID.equals(ZorzalElCaesar.DATA.ID) || temp.tags.contains(AbstractCard.CardTags.HEALING) || temp.tags.contains(GR.Enums.CardTags.VOLATILE))) {
                    choice.addToTop(temp.makeCopy());
                }
            }

            GameActions.Bottom.SelectFromPile(name, 1, choice)
                    .SetOptions(false, false)
                    .AddCallback(cards ->
                    {
                        GameActions.Bottom.MakeCardInDrawPile(cards.get(0));
                    });
        }
    }
}