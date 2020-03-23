package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class InverseOrigami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(InverseOrigami.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None);

    public InverseOrigami()
    {
        super(DATA);

        Initialize(0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.SpendEnergy(1, false)
        .AddCallback(() ->
        {
            if (upgraded)
            {
                GameActions.Bottom.MakeCardInHand(JavaUtilities.GetRandomElement(OrbCore.GetAllCores()).makeCopy());
            }
            else
            {
                GameActions.Bottom.ChannelRandomOrb(true);
            }

            GameActions.Bottom.Flash(this);
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        SupportDamagePower supportDamage = GameUtilities.GetPower(p, SupportDamagePower.class);
        if (supportDamage != null && supportDamage.amount > 0)
        {
            supportDamage.atEndOfTurn(true);
        }

        GameActions.Bottom.VFX(new RainbowCardEffect());
        GameActions.Bottom.Add(new TriggerOrbPassiveAbility(p.maxOrbs, false, true));
    }
}