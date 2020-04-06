package eatyourbeets.cards.animator.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Giselle extends AnimatorCard_UltraRare implements StartupCard
{
    public static final EYBCardData DATA = Register(Giselle.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Elemental).SetColor(CardColor.COLORLESS);

    public Giselle()
    {
        super(DATA);

        Initialize(24, 0, 4);
        SetUpgrade(8, 0, 0);
        SetScaling(0, 1, 2);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4f, m.hb.cY - m.hb.height / 4f));
        GameActions.Bottom.VFX(new FlameBarrierEffect(m.hb.cX, m.hb.cY), 0.5f);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.ChannelOrb(new Fire(), false);

        return true;
    }
}