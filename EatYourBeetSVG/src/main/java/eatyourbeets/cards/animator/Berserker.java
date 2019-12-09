package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Berserker extends AnimatorCard
{
    public static final String ID = Register(Berserker.class.getSimpleName(), EYBCardBadge.Special);

    public Berserker()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(26,0, 2, 12);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m != null)
        {
            GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F));
            GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY)
            .AddCallback(m.currentBlock, (initialBlock, target) ->
            {
                if (GameUtilities.IsDeadOrEscaped(target) || ((int)initialBlock > 0 && target.currentBlock <= 0))
                {
                    GameActions.Bottom.GainBlock(this.secondaryValue);
                }
            });
        }

        GameActions.Bottom.GainForce(magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(6);
        }
    }
}