package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.series.RozenMaiden.Suigintou;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Suigintou_BlackFeather extends AnimatorCard_Curse implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Suigintou_BlackFeather.class)
    		.SetCurse(0, EYBCardTarget.None, true)
            .SetSeries(CardSeries.RozenMaiden)
            .PostInitialize(data -> data.AddPreview(new Suigintou(), false));

    public Suigintou_BlackFeather()
    {
        super(DATA, false);

        Initialize(0, 0, 2, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(2, 0, 0);

        SetPurge(true);
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile, player.discardPile, player.exhaustPile)
                .SetFilter(c -> Suigintou.DATA.ID.equals(c.cardID));

        GameActions.Bottom.DealDamage(null, player, secondaryValue, DamageInfo.DamageType.THORNS, AttackEffects.DARKNESS);
        for (AbstractCreature cr : GameUtilities.GetEnemies(true)) {
            GameActions.Bottom.DealDamageAtEndOfTurn(player, cr, upgraded ? secondaryValue : magicNumber, AttackEffects.DARKNESS);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        super.OnStartOfTurnPostDraw();
        if (player.hand.contains(this)) {
            GameActions.Bottom.DealDamageAtEndOfTurn(player, player, magicNumber, AttackEffects.DARKNESS);
        }

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }
}
