package eatyourbeets.cards.animatorClassic.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnAfterCardDrawnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class MisakiNakahara extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(MisakiNakahara.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public MisakiNakahara()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetExhaust(true);
        this.series = CardSeries.WelcomeToNHK;
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardRarity rarity = rng.randomBoolean(0.25f) ? CardRarity.RARE : CardRarity.UNCOMMON;
        RandomizedList<AbstractCard> list = new RandomizedList<>(AbstractDungeon.colorlessCardPool.group);

        while (group.size() < magicNumber)
        {
            AbstractCard card = list.Retrieve(rng);
            if (card.rarity == rarity && !card.hasTag(CardTags.HEALING))
            {
                group.addToBottom(card.makeCopy());
            }
        }

        GameActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            if (cards != null && cards.size() > 0)
            {
                GameActions.Bottom.MakeCardInDrawPile(cards.get(0)).AddCallback(MotivateOnDraw::Register);
            }
        });
    }

    private static class MotivateOnDraw implements OnAfterCardDrawnSubscriber
    {
        private final AbstractCard card;

        private static void Register(AbstractCard card)
        {
            CombatStats.onAfterCardDrawn.Subscribe(new MotivateOnDraw(card));
        }

        private MotivateOnDraw(AbstractCard card)
        {
            this.card = card;
        }

        @Override
        public void OnAfterCardDrawn(AbstractCard other)
        {
            if (this.card.uuid.equals(other.uuid))
            {
                GameActions.Bottom.Flash(other);
                GameActions.Bottom.Motivate(1);
                CombatStats.onAfterCardDrawn.Unsubscribe(this);
            }
        }
    }
}