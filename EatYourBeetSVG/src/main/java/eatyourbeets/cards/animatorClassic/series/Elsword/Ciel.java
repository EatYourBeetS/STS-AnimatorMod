package eatyourbeets.cards.animatorClassic.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ciel extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Ciel.class).SetSeriesFromClassPackage().SetSkill(2, CardRarity.COMMON);
    static
    {
        DATA.AddPreview(new Lu(), true);
    }

    public Ciel()
    {
        super(DATA);

        Initialize(0, 4, 2, 6);
        SetUpgrade(0, 0, 0, 2);

        
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
            GameActions.Bottom.StackPower(new LockOnPower(m, magicNumber));
        }

        GameActions.Bottom.ModifyAllCopies(Lu.DATA.ID)
        .AddCallback(c ->
        {
            GameUtilities.IncreaseDamage(c, secondaryValue, false);
            c.flash();
        });
    }
}