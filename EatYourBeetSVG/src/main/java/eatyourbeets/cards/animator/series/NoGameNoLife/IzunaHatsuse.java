package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IzunaHatsuse extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IzunaHatsuse.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private boolean transformed;

    public IzunaHatsuse()
    {
        this(false);
    }

    private IzunaHatsuse(boolean transformed)
    {
        super(DATA);

        Initialize(1, 0, 3, 1);
        SetUpgrade(1, 1, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 1);

        SetHitCount(2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return upgraded ? super.GetBlockInfo() : null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.ApplyWeak(p, m, 1);
        if (upgraded)
        {
            GameActions.Bottom.GainBlock(block);
        }

        if (GameUtilities.GetHealthPercentage(player) < 0.2f) {
            GameActions.Bottom.RecoverHP(magicNumber);
        }
    }
}