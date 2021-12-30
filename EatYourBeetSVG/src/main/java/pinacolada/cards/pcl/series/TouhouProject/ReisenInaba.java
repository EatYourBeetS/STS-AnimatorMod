package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.actions.special.CreateRandomCurses;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ReisenInaba extends PCLCard
{
    public static final PCLCardData DATA = Register(ReisenInaba.class).SetSkill(0, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage(true);
    public static final int CARDS = 3;

    public ReisenInaba()
    {
        super(DATA);

        Initialize(0, 0, CARDS, 2);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return EnergyPanel.getCurrentEnergy() <= 0;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Add(new CreateRandomCurses(1, p.hand)).AddCallback(card -> {
            PCLActions.Bottom.ApplyPower(new ReisenInabaPower(player, card));
        });
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.ExhaustFromPile(name, secondaryValue, player.hand, player.discardPile, player.drawPile).SetOptions(false, true);
    }

    public static class ReisenInabaPower extends PCLPower
    {
        public AbstractCard card;

        public ReisenInabaPower(AbstractPlayer owner, AbstractCard card)
        {
            super(owner, ReisenInaba.DATA);

            this.card = card;
            updateDescription();
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            if (this.card == card) {
                flashWithoutSound();
                final RandomizedList<AbstractCard> pool = PCLGameUtilities.GetCardPoolInCombat(CardRarity.COMMON);
                pool.AddAll(PCLGameUtilities.GetCardPoolInCombat(CardRarity.UNCOMMON).GetInnerList());
                pool.AddAll(PCLGameUtilities.GetCardPoolInCombat(CardRarity.RARE).GetInnerList());
                final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while (choice.size() < CARDS && pool.Size() > 0)
                {
                    AbstractCard temp = pool.Retrieve(rng);
                    if (!(temp.cardID.equals(card.cardID) || temp.tags.contains(AbstractCard.CardTags.HEALING) || temp.tags.contains(GR.Enums.CardTags.VOLATILE))) {
                        AbstractCard temp2 = temp.makeCopy();
                        PCLGameUtilities.ModifyCostForCombat(temp2, -1, true);
                        choice.addToTop(temp2);
                    }
                }

                PCLActions.Bottom.SelectFromPile(name, 1, choice)
                        .SetOptions(false, false)
                        .AddCallback(cards ->
                        {
                            PCLActions.Bottom.MakeCardInHand(cards.get(0));
                            PCLActions.Bottom.Add(new RefreshHandLayout());
                        });
                RemovePower();
            }

        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, card != null ? card.name.replace(" ", " #y") : "");
        }
    }
}

