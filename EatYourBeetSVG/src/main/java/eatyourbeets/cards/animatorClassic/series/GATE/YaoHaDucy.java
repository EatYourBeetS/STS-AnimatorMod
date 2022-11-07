package eatyourbeets.cards.animatorClassic.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YaoHaDucy extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(YaoHaDucy.class).SetSeriesFromClassPackage().SetAttack(0, CardRarity.COMMON);

    public YaoHaDucy()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(3, 0, 0, 0);


    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return (other.freeToPlay() || other.costForTurn == 0) || super.HasDirectSynergy(other);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        }
    }
}