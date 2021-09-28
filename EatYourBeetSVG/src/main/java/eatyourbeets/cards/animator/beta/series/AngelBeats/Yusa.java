package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Yusa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yusa.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public Yusa()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Top.Scry(secondaryValue).AddCallback(() -> {
            GameActions.Top.ExhaustFromPile(name, magicNumber, p.discardPile).AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    if (GameUtilities.HasLightAffinity(c)) {
                        GameActions.Bottom.GainBlessing(1);
                    }
                }
            });
        });
    }
}