package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Berserker extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Berserker.class).SetAttack(3, CardRarity.COMMON);

    public Berserker()
    {
        super(DATA);

        Initialize(23, 0, 2, 12);
        SetUpgrade(0, 0, 1, 2);
        SetScaling(0, 0, 3);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m != null)
        {
            GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4f, m.hb.cY - m.hb.height / 4f));
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY)
            .AddCallback(m.currentBlock, (initialBlock, target) ->
            {
                if (GameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0))
                {
                    GameActions.Bottom.GainBlock(this.secondaryValue);
                }
            });
            GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }

        GameActions.Bottom.GainForce(magicNumber);
    }
}