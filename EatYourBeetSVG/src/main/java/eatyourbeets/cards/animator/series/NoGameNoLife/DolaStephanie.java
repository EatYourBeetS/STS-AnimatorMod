package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class DolaStephanie extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaStephanie.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public DolaStephanie()
    {
        super(DATA);

        Initialize(0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.NoGameNoLife);
        SetAffinity(0, 0, 1, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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
                .SetFilter(c -> Synergies.WouldSynergize(selected, c)); //
            }
        });
    }
}