package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public abstract class AbstractMysteryCard extends PCLCard
{
    public CardRarity[] rarities;

    public AbstractMysteryCard(PCLCardData data, boolean isDummy, CardRarity... rarities)
    {
        super(data);
        SetObtainableInCombat(false);
        this.rarities = rarities;

        Initialize(0, 0, 1, 0);
        if (isDummy) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        }

        SetVolatile(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Last.ReplaceCard(uuid,CreateObscuredCard());
    }

    public final AbstractCard CreateObscuredCard() {
        ArrayList<AbstractCard> pool = GetPool(this.rarities);
        WeightedList<AbstractCard> possiblePicks = new WeightedList<>();
        for (AbstractCard c : pool) {
            possiblePicks.Add(c, GetWeight(c));
        }
        AbstractCard card = possiblePicks.Retrieve(rng).makeCopy();
        if (upgraded) {
            card.upgrade();
        }
        card.cost = card.costForTurn = this.cost;
        return card;
    }

    private ArrayList<AbstractCard> GetPool(CardRarity... rarities) {
        ArrayList<AbstractCard> pool = new ArrayList<>();
        for (CardRarity rarity : rarities) {
            switch (rarity) {
                case COMMON:
                    pool.addAll(AbstractDungeon.commonCardPool.group);
                    break;
                case UNCOMMON:
                    pool.addAll(AbstractDungeon.commonCardPool.group);
                    break;
                case RARE:
                    pool.addAll(AbstractDungeon.commonCardPool.group);
                    break;
                case CURSE:
                    pool.addAll(AbstractDungeon.commonCardPool.group);
                    break;
            }
        }
        return PCLJUtils.Filter(pool, this::CheckCondition);
    }

    private boolean CheckCondition(AbstractCard c) {
        return c.cost <= magicNumber && c.cost >= 0
                && !c.purgeOnUse
                && !(c instanceof PCLCard_UltraRare);
    }

    private int GetWeight(AbstractCard c) {
        return 10 - Math.max(2,2 * c.cost);
    }
}