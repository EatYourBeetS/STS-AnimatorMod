package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.animator.ChlammyZellPower;
import eatyourbeets.utilities.GameActions;

public class ChlammyZellScheme extends AnimatorCard implements Hidden
{
    public static final String ID = Register(ChlammyZellScheme.class);

    public ChlammyZellScheme()
    {
        super(ID, 1, CardRarity.SPECIAL, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 5);
        SetCostUpgrade(-1);

        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new FlickCoinEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY + p.hb.height), 0.15F);
        GameActions.Bottom.StackPower(new ChlammyZellPower(p, magicNumber));
    }
}