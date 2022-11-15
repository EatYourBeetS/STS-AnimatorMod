package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Berserker extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Berserker.class).SetSeriesFromClassPackage().SetAttack(3, CardRarity.COMMON);

    public Berserker()
    {
        super(DATA);

        Initialize(18, 0, 2, 12);
        SetUpgrade(6, 0, 0, 0);
        SetScaling(0, 0, 3);


    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (m != null)
        {
            GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4f, m.hb.cY - m.hb.height / 4f));
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY)
            .AddCallback(m.currentBlock, (initialBlock, target) ->
            {
                if (GameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0))
                {
                    GameActions.Bottom.GainBlock(this.secondaryValue);
                }
            });
            GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }

        if (ForceStance.IsActive())
        {
            GameActions.Bottom.GainForce(magicNumber);
        }
        else
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        }
    }
}