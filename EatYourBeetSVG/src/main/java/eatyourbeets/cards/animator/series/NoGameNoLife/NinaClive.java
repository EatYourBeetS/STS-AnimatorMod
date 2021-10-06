package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class NinaClive extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NinaClive.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public NinaClive()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromPile(name, upgraded ? GetHandAffinity(Affinity.General,false) : 1, player.hand, player.discardPile)
        .SetOptions(false, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards) {
                GameActions.Bottom.ModifyTag(c,DELAYED,true);
            }

            if (cards.size() > 0) {
                GameActions.Bottom.SelectFromPile(name, cards.size(), player.hand)
                        .SetOptions(false, true)
                        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[1])
                        .AddCallback(cards2 -> {
                            for (AbstractCard c2 : cards2) {
                                GameActions.Bottom.ModifyTag(c2,ANIMATOR_INNATE,true);
                            }
                        });
            }
        });
    }


}