package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
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

public class InverseOrigami extends AnimatorCard {
    public static final EYBCardData DATA = Register(InverseOrigami.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public InverseOrigami() {
        super(DATA);

        Initialize(0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int energy = EnergyPanel.getCurrentEnergy();
        if (energy > 0)
        {
            GameActions.Bottom.GainEnergy(-1);

            if (upgraded)
            {
                GameActions.Bottom.MakeCardInHand(JavaUtilities.GetRandomElement(OrbCore.GetAllCores()).makeCopy());
            }
            else
            {
                GameActions.Bottom.ChannelRandomOrb(true);
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int numOrbsTriggered = 0;

        SupportDamagePower supportDamage = GameUtilities.GetPower(p, SupportDamagePower.class);
        if (supportDamage != null && supportDamage.amount > 0)
        {
            supportDamage.atEndOfTurn(true);
        }

        for (AbstractOrb orb: p.orbs ) {

            if (GameUtilities.IsValidOrb(orb))
            {
                orb.onStartOfTurn();
                orb.onEndOfTurn();
            }

            numOrbsTriggered++;
        }
    }
}