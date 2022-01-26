package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Ushio extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Ushio.class).SetSkill(0, CardRarity.RARE, PCLCardTarget.None).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.Clannad)
            .PostInitialize(data -> data.AddPreview(new GarbageDoll(), false));

    private int turns;

    public Ushio()
    {
        super(DATA);

        Initialize(0, 0, 4, 6);
        SetUpgrade(0, 0, 1,0);

        SetAffinity_Light(1);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);

        if (CheckSpecialCondition(true) && CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.MakeCardInDiscardPile(new GarbageDoll()).SetUpgrade(upgraded, false);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        PCLGameEffects.Queue.ShowCardBriefly(this);
        PCLActions.Bottom.SFX("POWER_TIME_WARP", 0.05F, 0.05F);
        PCLActions.Bottom.VFX(new TimeWarpTurnEndEffect());
        PCLActions.Bottom.DrawNextTurn(magicNumber);
        PCLActions.Bottom.GainEnergyNextTurn(secondaryValue);
        PCLActions.Bottom.Add(new PressEndTurnButtonAction());
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return player.exhaustPile.size() > player.drawPile.size();
    }
}