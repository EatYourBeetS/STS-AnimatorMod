package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Curse_GriefSeed extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_GriefSeed.class).SetCurse(1, EYBCardTarget.None);

    public Curse_GriefSeed()
    {
        super(DATA, false);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.Callback(() ->
        {
            for (int i = player.powers.size() - 1; i >= 0; i--)
            {
                AbstractPower power = player.powers.get(i);
                if (power.type == AbstractPower.PowerType.DEBUFF)
                {
                    GameActions.Top.RemovePower(player, player, power);
                    return;
                }
            }
        });
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        GameActions.Bottom.Callback(() ->
        {
            GameActions.Bottom.Flash(this);
            GameActions.Bottom.DealDamage(null, player, magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

    }
}
