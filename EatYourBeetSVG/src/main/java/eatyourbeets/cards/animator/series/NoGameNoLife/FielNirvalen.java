package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class FielNirvalen extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FielNirvalen.class).SetPower(1, CardRarity.UNCOMMON).SetMaxCopies(3);
    public static final int SCRY_AMOUNT = 2;

    public FielNirvalen()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.addToBottom(CreateChoice(text[1], (c1, p1, m1) -> GameActions.Bottom.GainAgility(1, true)));
        group.addToBottom(CreateChoice(text[2], (c1, p1, m1) -> GameActions.Bottom.GainIntellect(1, true)));
        group.addToBottom(CreateChoice(text[3], (c1, p1, m1) -> GameActions.Bottom.GainForce(1, true)));

        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.StackPower(new FielNirvalenPower(p, SCRY_AMOUNT));
        GameActions.Bottom.SelectFromPile(name, secondaryValue, group)
        .SetOptions(false, false)
        .SetMessage(CardRewardScreen.TEXT[1])
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                card.use(player, null);
            }
        });
    }

    private AnimatorCard_Dynamic CreateChoice(String text, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onSelect)
    {
        return new AnimatorCardBuilder(cardID)
        .SetImage(assetUrl)
        .SetProperties(CardType.SKILL, rarity, CardTarget.NONE)
        .SetCost(-2, 0)
        .SetOnUse(onSelect)
        .SetText(name, text, text)
        .SetSynergy(synergy, false).Build();
    }

    public static class FielNirvalenPower extends AnimatorPower implements OnShuffleSubscriber
    {
        public FielNirvalenPower(AbstractCreature owner, int amount)
        {
            super(owner, FielNirvalen.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            CombatStats.onShuffle.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            CombatStats.onShuffle.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            enabled = true;
        }

        @Override
        public void OnShuffle(boolean triggerRelics)
        {
            if (!owner.powers.contains(this))
            {
                CombatStats.onShuffle.Unsubscribe(this);
                return;
            }

            if (enabled)
            {
                this.flash();
                GameActions.Bottom.Scry(amount);
                enabled = false;
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}