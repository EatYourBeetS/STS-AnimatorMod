package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ZorzalElCaesar extends PCLCard
{
    public static final PCLCardData DATA = Register(ZorzalElCaesar.class)
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
        PCLActions.Bottom.StackPower(new ZorzalElCaesarPower(p, secondaryValue));
    }

    public static class ZorzalElCaesarPower extends PCLClickablePower
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

            PCLGameUtilities.ModifyCardDrawPerTurn(-DRAW_REDUCTION, 1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLGameUtilities.ModifyCardDrawPerTurn(DRAW_REDUCTION, 1);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            PCLActions.Bottom.SelectFromPile(name, Integer.MAX_VALUE, player.exhaustPile, player.drawPile, player.discardPile, player.hand)
            .SetOptions(false, false)
            .SetFilter(PCLGameUtilities::HasRedAffinity)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    ((PCLCard)c).SetHaste(true);
                }
            });

            flashWithoutSound();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            final RandomizedList<AbstractCard> pool = PCLGameUtilities.GetCardPoolInCombat(CardRarity.UNCOMMON);
            final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            while (choice.size() < 3 && pool.Size() > 0)
            {
                AbstractCard temp = pool.Retrieve(rng);
                if (temp.cost == 0 && !(temp.cardID.equals(ZorzalElCaesar.DATA.ID) || temp.tags.contains(AbstractCard.CardTags.HEALING) || temp.tags.contains(GR.Enums.CardTags.VOLATILE))) {
                    choice.addToTop(temp.makeCopy());
                }
            }

            PCLActions.Bottom.SelectFromPile(name, 1, choice)
                    .SetOptions(false, false)
                    .AddCallback(cards ->
                    {
                        PCLActions.Bottom.MakeCardInDrawPile(cards.get(0));
                    });
        }
    }
}