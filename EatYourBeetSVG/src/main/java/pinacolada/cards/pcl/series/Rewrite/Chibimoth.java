package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.interfaces.delegates.ActionT3;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Chibimoth extends PCLCard
{
    public static final PCLCardData DATA = Register(Chibimoth.class).SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new KotoriKanbe(), false));

    public Chibimoth()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0,0,1,1);
        SetAffinity_Star(1, 0, 0);
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
            PCLActions.Bottom.GainVelocity(magicNumber, false);
            PCLActions.Bottom.GainSupportDamage(secondaryValue);
        }));
        group.addToBottom(CreateChoice(text[1], (c1, p1, m1) ->
        {
            PCLActions.Bottom.GainEndurance(magicNumber, false);
            PCLActions.Bottom.GainThorns(secondaryValue);
        }));

        PCLActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .SetMessage(CardRewardScreen.TEXT[1])
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                card.use(player, null);
            }
        });

        PCLActions.Bottom.Callback(() ->
        {
            if (!DrawKotoriKanbe(player.drawPile))
            {
                DrawKotoriKanbe(player.discardPile);
            }
        });
    }

    private PCLCard_Dynamic CreateChoice(String text, ActionT3<PCLCard, AbstractPlayer, AbstractMonster> onSelect)
    {
        return new PCLCardBuilder(cardID)
        .SetImagePath(assetUrl)
        .SetProperties(CardType.SKILL, rarity, CardTarget.NONE)
        .SetCost(-2, 0)
        .SetNumbers(0,0,magicNumber,secondaryValue, 1)
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
                    PCLGameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    PCLActions.Top.MoveCard(c, group, player.hand)
                    .ShowEffect(true, true);
                }

                return true;
            }
        }

        return false;
    }
}