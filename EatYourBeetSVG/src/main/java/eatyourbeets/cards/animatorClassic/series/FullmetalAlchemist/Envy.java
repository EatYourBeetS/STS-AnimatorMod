package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.animatorClassic.EnvyPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Envy extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Envy.class).SetSeriesFromClassPackage().SetPower(2, CardRarity.RARE);

    public Envy()
    {
        super(DATA);

        Initialize(0, 0);

        SetEthereal(true);
        
        SetShapeshifter();
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (magicNumber > 0) ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifyMagicNumber(this, Math.floorDiv(player.maxHealth - player.currentHealth, 5), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EnvyPower(p, 1));

        int tempHP = Math.floorDiv(p.maxHealth - p.currentHealth, 5);
        if (tempHP > 0)
        {
            GameActions.Bottom.GainTemporaryHP(tempHP);
        }
    }
}