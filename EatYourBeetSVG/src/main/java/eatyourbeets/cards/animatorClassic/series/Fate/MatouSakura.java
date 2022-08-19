package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class MatouSakura extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(MatouSakura.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL);

    public MatouSakura()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);

        SetEthereal(true);
        SetExhaust(true);
        SetSeries(CardSeries.Fate);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        int darkOrbs = JUtils.Count(player.orbs, Dark.class::isInstance);
        if (darkOrbs > 0)
        {
            GameUtilities.IncreaseMagicNumber(this, darkOrbs * secondaryValue, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyConstricted(TargetHelper.Enemies(p), magicNumber);
    }
}