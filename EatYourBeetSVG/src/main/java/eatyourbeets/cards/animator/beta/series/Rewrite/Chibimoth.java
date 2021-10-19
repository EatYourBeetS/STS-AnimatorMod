package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Chibimoth extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chibimoth.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new KotoriKanbe(), false));

    public Chibimoth()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0,0,1,0);
        SetAffinity_Star(1, 1, 0);
        SetLoyal(true);
        SetExhaust(true);
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.addToBottom(CreateChoice(text[0], (c1, p1, m1) ->
        {
            GameActions.Bottom.GainAgility(magicNumber, false);
            GameActions.Bottom.GainSupportDamage(secondaryValue);
        }));
        group.addToBottom(CreateChoice(text[1], (c1, p1, m1) ->
        {
            GameActions.Bottom.GainWillpower(magicNumber, false);
            GameActions.Bottom.GainThorns(secondaryValue);
        }));

        GameActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .SetMessage(CardRewardScreen.TEXT[1])
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                card.use(player, null);
            }
        });

        GameActions.Bottom.Callback(() ->
        {
            if (!DrawKotoriKanbe(player.drawPile))
            {
                DrawKotoriKanbe(player.discardPile);
            }
        });
    }

    private AnimatorCard_Dynamic CreateChoice(String text, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onSelect)
    {
        return new AnimatorCardBuilder(cardID)
        .SetImagePath(assetUrl)
        .SetProperties(CardType.SKILL, rarity, CardTarget.NONE)
        .SetCost(-2, 0)
        .SetNumbers(0,0,magicNumber,secondaryValue)
        .SetOnUse(onSelect)
        .SetText(name, text, text).Build();
    }

    private boolean DrawKotoriKanbe(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (KotoriKanbe.DATA.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    GameActions.Top.MoveCard(c, group, player.hand)
                    .ShowEffect(true, true);
                }

                return true;
            }
        }

        return false;
    }
}