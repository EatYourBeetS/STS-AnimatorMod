package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnAfterCardDrawnSubscriber;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class MisakiNakahara extends PCLCard
{
    public static final PCLCardData DATA = Register(MisakiNakahara.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.WelcomeToNHK);

    public MisakiNakahara()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Light(1);
        SetAffinity_Orange(1);

        SetExhaust(true);
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

        PCLActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            if (cards != null && cards.size() > 0)
            {
                PCLActions.Bottom.MakeCardInDrawPile(cards.get(0)).AddCallback(MotivateOnDraw::Register);
            }
        });
    }

    private static class MotivateOnDraw implements OnAfterCardDrawnSubscriber
    {
        private final AbstractCard card;

        private static void Register(AbstractCard card)
        {
            PCLCombatStats.onAfterCardDrawn.Subscribe(new MotivateOnDraw(card));
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
                PCLActions.Bottom.Flash(other);
                PCLActions.Bottom.Motivate(1);
                PCLCombatStats.onAfterCardDrawn.Unsubscribe(this);
            }
        }
    }
}