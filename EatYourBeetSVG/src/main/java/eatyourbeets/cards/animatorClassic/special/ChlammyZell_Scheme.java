package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.animator.ChlammyZellPower;
import eatyourbeets.utilities.GameActions;

public class ChlammyZell_Scheme extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ChlammyZell_Scheme.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public ChlammyZell_Scheme()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetCostUpgrade(-1);

        SetRetain(true);
        SetExhaust(true);
        SetSeries(CardSeries.NoGameNoLife);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new FlickCoinEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY + p.hb.height), 0.15f);
        GameActions.Bottom.StackPower(new ChlammyZellPower(p, magicNumber));
    }
}