package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AdrenalineEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;

public class DarknessAdrenaline extends AnimatorCard implements Hidden
{
    public static final String ID = Register(DarknessAdrenaline.class);

    public DarknessAdrenaline()
    {
        super(ID, 0, CardRarity.SPECIAL, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new AdrenalineEffect(), 0.15F);
        GameActions.Bottom.GainEnergy(secondaryValue);
        GameActions.Bottom.Draw(magicNumber);
    }
}