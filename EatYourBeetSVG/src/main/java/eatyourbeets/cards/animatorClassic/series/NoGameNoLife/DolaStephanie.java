package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class DolaStephanie extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(DolaStephanie.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public DolaStephanie()
    {
        super(DATA);

        Initialize(0, 0);

        SetExhaust(true);
        SetSeries(CardSeries.NoGameNoLife);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                AbstractCard selected = cards.get(0);
                GameActions.Top.FetchFromPile(name, 1, player.drawPile)
                .SetOptions(false, false)
                .SetFilter(selected, CardSeries.Synergy::WouldSynergize);
            }
        });
    }
}