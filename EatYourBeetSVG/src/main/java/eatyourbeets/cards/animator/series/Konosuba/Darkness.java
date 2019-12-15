package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.animator.special.DarknessAdrenaline;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.DarknessPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class Darkness extends AnimatorCard
{
    public static final String ID = Register(Darkness.class.getSimpleName(), EYBCardBadge.Special);

    public Darkness()
    {
        super(ID, 1, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,2,2);

        SetSynergy(Synergies.Konosuba);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new DarknessAdrenaline(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new PlatedArmorPower(p, this.magicNumber));
        GameActions.Bottom.StackPower(new DarknessPower(p, 1));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
            upgradeMagicNumber(1);
        }
    }
}