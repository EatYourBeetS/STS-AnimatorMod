package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public abstract class MysteryCard extends AnimatorCard
{
    public static final String ID = GR.Animator.CreateID(MysteryCard.class.getSimpleName());
    public CardRarity rarity;

    public MysteryCard(EYBCardData data, int synergiesRequired)
    {
        super(data);

        Initialize(0, 0, 0, 0);

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        RandomizedList<AbstractCard> possiblePicks = new RandomizedList<>(JUtils.Filter(AbstractDungeon.commonCardPool.group, this::CheckCondition));
    }


    public boolean CheckCondition(AbstractCard c) {
        return c.cost >= magicNumber
                && !c.purgeOnUse
                && !(c instanceof AnimatorCard_UltraRare);
    }
}