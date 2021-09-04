package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class Kirby_MysteryCard extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kirby_MysteryCard.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS);

    public CardRarity rarity;

    public Kirby_MysteryCard() {
        this(false);
    }

    public Kirby_MysteryCard(boolean isDummy)
    {
        super(DATA);
        SetObtainableInCombat(false);

        Initialize(0, 0, 1, 0);
        if (isDummy) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Last.ReplaceCard(uuid,CreateObscuredCard());
    }

    public AbstractCard CreateObscuredCard() {
        RandomizedList<AbstractCard> possiblePicks = new RandomizedList<>(JUtils.Filter(AbstractDungeon.commonCardPool.group, this::CheckCondition));
        AbstractCard card = possiblePicks.Retrieve(rng).makeCopy();
        if (upgraded) {
            card.upgrade();
        }
        card.cost = card.costForTurn = this.cost;
        return card;
    }

    public boolean CheckCondition(AbstractCard c) {
        return c.cost <= magicNumber && c.cost >= 0
                && !c.purgeOnUse
                && !(c instanceof AnimatorCard_UltraRare);
    }
}