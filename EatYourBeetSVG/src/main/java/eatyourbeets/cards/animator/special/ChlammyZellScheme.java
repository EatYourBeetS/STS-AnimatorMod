package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.animator.ChlammyZellPower;
import eatyourbeets.utilities.GameActions;

public class ChlammyZellScheme extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChlammyZellScheme.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public ChlammyZellScheme()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetCostUpgrade(-1);

        SetRetain(true);
        SetExhaust(true);
        SetSeries(CardSeries.NoGameNoLife);
        SetAffinity(0, 0, 2, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new FlickCoinEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY + p.hb.height), 0.15f);
        GameActions.Bottom.StackPower(new ChlammyZellPower(p, magicNumber));
    }
}