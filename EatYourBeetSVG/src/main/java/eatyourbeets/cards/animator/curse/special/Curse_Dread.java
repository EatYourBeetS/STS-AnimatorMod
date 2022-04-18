package eatyourbeets.cards.animator.curse.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Curse_Dread extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Curse_Dread.class)
            .SetCurse(0, EYBCardTarget.None, true)
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.HealAndObtainCurse(data, 2, 10));
                EYBCardPopupActions.PermanentActions.add(new Curse_Dread());
            });

    public Curse_Dread()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
        SetExhaust(true);
        SetDelayed(true);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameActions.Delayed.Callback(() ->
        {
           if (player.drawPile.contains(this))
           {
               GameActions.Bottom.LoseHP(magicNumber, AttackEffects.DARK).CanKill(false);
               GameEffects.Queue.ShowCopy(this, Settings.WIDTH * 0.35f, Settings.HEIGHT * 0.5f);
           }
        });
    }

    @Override
    public void triggerOnExhaust()
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainCorruption(secondaryValue);
            GameActions.Bottom.Callback(() -> player.increaseMaxHp(1, true));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }
}
