package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.animator.series.NoGameNoLife.FielNirvalen;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.GameActions;

public class Chibimoth extends AnimatorCard {
    public static final EYBCardData DATA = Register(Chibimoth.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Chibimoth() {
        super(DATA);

        Initialize(0, 0, 1);
        SetExhaust(true);
        SetRetain(true);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.addToBottom(CreateChoice(text[0], (c1, p1, m1) -> GameActions.Bottom.GainAgility(1, false)));
        group.addToBottom(CreateChoice(text[1], (c1, p1, m1) -> GameActions.Bottom.GainIntellect(1, false)));
        group.addToBottom(CreateChoice(text[2], (c1, p1, m1) -> GameActions.Bottom.GainForce(1, false)));

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
}