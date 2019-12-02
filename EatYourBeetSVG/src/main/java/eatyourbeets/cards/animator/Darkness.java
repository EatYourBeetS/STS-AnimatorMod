package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.DarknessPower;
import eatyourbeets.utilities.GameActionsHelper;

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

//    @Override
//    public List<TooltipInfo> getCustomTooltips()
//    {
//        if (cardText.index == 1)
//        {
//            return super.getCustomTooltips();
//        }
//
//        return null;
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, block);
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new DarknessPower(p, 1), 1);
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