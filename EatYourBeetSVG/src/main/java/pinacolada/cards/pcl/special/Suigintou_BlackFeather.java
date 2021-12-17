package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.cards.pcl.series.RozenMaiden.Suigintou;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Suigintou_BlackFeather extends PCLCard_Curse implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Suigintou_BlackFeather.class)
    		.SetCurse(0, eatyourbeets.cards.base.EYBCardTarget.None, true)
            .SetSeries(CardSeries.RozenMaiden)
            .PostInitialize(data -> data.AddPreview(new Suigintou(), false));

    public Suigintou_BlackFeather()
    {
        super(DATA, false);

        Initialize(0, 0, 2, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetPurge(true);
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile, player.discardPile, player.exhaustPile)
                .SetFilter(c -> Suigintou.DATA.ID.equals(c.cardID));

        PCLActions.Bottom.DealDamage(null, player, secondaryValue, DamageInfo.DamageType.THORNS, AttackEffects.DARKNESS);
        for (AbstractCreature cr : PCLGameUtilities.GetEnemies(true)) {
            PCLActions.Bottom.DealDamageAtEndOfTurn(player, cr, upgraded ? secondaryValue : magicNumber, AttackEffects.DARKNESS);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        super.OnStartOfTurnPostDraw();
        if (player.hand.contains(this)) {
            PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, magicNumber, AttackEffects.DARKNESS);
        }

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }
}
