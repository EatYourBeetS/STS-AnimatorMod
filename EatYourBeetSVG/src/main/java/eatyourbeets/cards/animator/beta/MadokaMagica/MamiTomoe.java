package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.scene.LightFlareLEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MamiTomoe extends AnimatorCard {
    public static final EYBCardData DATA = Register(MamiTomoe.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal);

    public MamiTomoe()
    {
        super(DATA);

        Initialize(0, 0,12);
        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        int damageCount = magicNumber * GameUtilities.GetCurseCount(abstractPlayer.discardPile);

        GameActions.Bottom.VFX(new LightFlareLEffect(abstractMonster.hb_x, abstractMonster.hb_y));

        GameActions.Bottom.DealDamage(abstractPlayer, abstractMonster, damageCount, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        GameActions.Bottom.Add(new PressEndTurnButtonAction());
    }
}
