package eatyourbeets.cards.animatorClassic.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.vfx.ColoredSweepingBeamEffect;
import eatyourbeets.utilities.GameActions;

public class Kuroyukihime_BlackLotus extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Kuroyukihime_BlackLotus.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);

    public Kuroyukihime_BlackLotus()
    {
        super(DATA);

        Initialize(7, 5, 1);
        SetUpgrade(0, 0, 1);

        this.series = CardSeries.AccelWorld;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SFX("ATTACK_DEFECT_BEAM");
        GameActions.Bottom.VFX(new ColoredSweepingBeamEffect(p.hb.cX, p.hb.cY, p.flipHorizontal, Color.valueOf("3d0066")), 0.3f);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
        GameActions.Bottom.GainBlur(magicNumber);
    }
}