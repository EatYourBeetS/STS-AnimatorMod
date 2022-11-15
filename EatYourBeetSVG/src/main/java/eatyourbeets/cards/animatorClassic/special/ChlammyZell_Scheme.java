package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.ChlammyZellPower;
import eatyourbeets.utilities.GameActions;

public class ChlammyZell_Scheme extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ChlammyZell_Scheme.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.NoGameNoLife);

    public ChlammyZell_Scheme()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetCostUpgrade(-1);

        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new FlickCoinEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY + p.hb.height), 0.15f);
        GameActions.Bottom.StackPower(new ChlammyZellPower(p, magicNumber));
    }
}