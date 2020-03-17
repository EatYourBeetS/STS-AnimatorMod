package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class DolaStephanie extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaStephanie.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public DolaStephanie()
    {
        super(DATA);

        Initialize(0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .SetFilter(c -> c instanceof AnimatorCard)
        .AddCallback(cards ->
        {
            AnimatorCard card = JavaUtilities.SafeCast(cards.get(0), AnimatorCard.class);
            if (card != null)
            {
                GameActions.Top.FetchFromPile(name, 1, player.drawPile)
                .SetOptions(false, false)
                .SetFilter(card::HasSynergy);
            }
        });
    }
}