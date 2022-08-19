package eatyourbeets.cards.animatorClassic.curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Curse_GriefSeed extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Curse_GriefSeed.class).SetCurse(1, EYBCardTarget.None, false);

    public Curse_GriefSeed()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetEndOfTurnPlay(false);
        SetSeries(CardSeries.MadokaMagica);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.WaitRealtime(0.3f);
        GameActions.Bottom.Callback(() ->
        {
            RandomizedList<AbstractPower> powers = new RandomizedList<>();
            for (AbstractPower p : player.powers)
            {
                if (p.type == AbstractPower.PowerType.DEBUFF)
                {
                    powers.Add(p);
                }
            }

            if (powers.Size() > 0)
            {
                GameActions.Top.RemovePower(player, powers.Retrieve(rng));
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}
