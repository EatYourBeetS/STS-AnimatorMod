package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.common.PhasingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NaoTomori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NaoTomori.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public NaoTomori()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);

        SetSeries(CardSeries.Charlotte);
        SetAffinity_Green(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < magicNumber)
        {
            AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat();
            if (group.findCardById(card.cardID) == null)
            {
                group.addToBottom(card.makeCopy());
            }
        }

        GameActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.MakeCardInHand(cards.get(0));
                GameUtilities.RefreshHandLayout();
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetFilter(c -> c.type.equals(CardType.POWER) || c.type.equals(CardType.STATUS))
        .SetOptions(false, false, false)
        .SetMessage(DATA.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.ReplaceCard(cards.get(0).uuid, new Normality());
                GameActions.Bottom.StackPower(new PhasingPower(player, secondaryValue));
            }
        });
    }
}