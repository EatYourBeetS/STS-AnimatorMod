package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.AcuraTooru;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class AcuraTooru_Dragoon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraTooru_Dragoon.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(AcuraTooru.DATA.Series)
            .SetMaxCopies(1);

    public AcuraTooru_Dragoon()
    {
        super(DATA);

        Initialize(10, 0);
        SetUpgrade(2, 0);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Green(1, 1, 2);

        SetCardPreview(c -> c.hasTag(CardTags.STRIKE) || c.hasTag(CardTags.STARTER_STRIKE));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.GainAgility(1, true);
        GameActions.Bottom.PlayFromPile(name, 1, m, p.drawPile).SetExhaust(true)
        .SetFilter(c -> c.hasTag(CardTags.STRIKE) || c.hasTag(CardTags.STARTER_STRIKE))
        .SetOptions(CardSelection.Top, false);
    }
}