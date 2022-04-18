package eatyourbeets.cards.animator.curse.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.Mathf;

public class Curse_GriefSeed extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_GriefSeed.class)
            .SetCurse(1, EYBCardTarget.None, true)
            .SetSeries(CardSeries.MadokaMagica);

    public Curse_GriefSeed()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.ModifyDebuffs(player, p ->
        {
            final int h = Mathf.CeilToInt(Mathf.Abs(p.amount) / 2f);
            return p.amount + (p.amount >= 0 ? -h : +h);
        }, null, 999);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.TakeDamageAtEndOfTurn(magicNumber, AttackEffects.DARK);
        }
    }
}
