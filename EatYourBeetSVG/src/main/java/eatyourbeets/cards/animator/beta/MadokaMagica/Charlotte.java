package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.attack.Hemokinesis2Effect;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Charlotte extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Charlotte.class).SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Normal);

    public Charlotte()
    {
        super(DATA);

        Initialize(8, 0, 0);
        SetCostUpgrade(-1);
        SetScaling(1,0,0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage * (float) Math.pow(2, player.hand.getCardsOfType(CardType.CURSE).size());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(e ->
        {
            GameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40.0F * Settings.scale, Color.WHITE.cpy()));

            if (damage > 30)
            {
                GameEffects.List.Add(new Hemokinesis2Effect(e.hb.cX, e.hb.cY, player.hb.cX, player.hb.cY));
                GameEffects.List.Add(new BorderFlashEffect(Color.RED));
                GameActions.Top.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
            }
        });
    }
}