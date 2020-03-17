package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnAfterCardDrawnSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class MisakiNakahara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MisakiNakahara.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public MisakiNakahara()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetExhaust(true);
        SetSynergy(Synergies.WelcomeToNHK);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
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
        .SetMessage(CardRewardScreen.TEXT[1])
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
            PlayerStatistics.onAfterCardDrawn.Subscribe(new MotivateOnDraw(card));
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
                PlayerStatistics.onAfterCardDrawn.Unsubscribe(this);
            }
        }
    }
}