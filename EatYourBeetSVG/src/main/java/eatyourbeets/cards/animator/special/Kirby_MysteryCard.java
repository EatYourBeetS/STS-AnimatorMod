package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class Kirby_MysteryCard extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kirby_MysteryCard.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS);

    public CardRarity rarity;

    public Kirby_MysteryCard()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);

    }
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        ChangeIntoCard();
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        ChangeIntoCard();
    }

    private void ChangeIntoCard() {
        RandomizedList<AbstractCard> possiblePicks = new RandomizedList<>(JUtils.Filter(AbstractDungeon.commonCardPool.group, this::CheckCondition));
        AbstractCard card = possiblePicks.Retrieve(rng).makeCopy();
        GameActions.Bottom.ReplaceCard(uuid,card).AddCallback(()-> {
            GameUtilities.CopyVisualProperties(this, card);
            card.cost = card.costForTurn = this.cost;
        });
    }

    public boolean CheckCondition(AbstractCard c) {
        return c.cost <= magicNumber && c.cost >= 0
                && !c.purgeOnUse
                && !(c instanceof AnimatorCard_UltraRare);
    }
}