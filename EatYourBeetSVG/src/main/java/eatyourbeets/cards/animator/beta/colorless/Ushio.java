package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Ushio extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Ushio.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.Clannad)
            .PostInitialize(data -> data.AddPreview(new GarbageDoll(), false));

    private int turns;

    public Ushio()
    {
        super(DATA);

        Initialize(0, 0, 4, 6);
        SetUpgrade(0, 0, 1,0);

        SetAffinity_Light(2);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);

        if (CheckSpecialCondition(true) && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.MakeCardInDiscardPile(new GarbageDoll()).SetUpgrade(upgraded, false);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameEffects.Queue.ShowCardBriefly(this);
        GameActions.Bottom.SFX("POWER_TIME_WARP", 0.05F, 0.05F);
        GameActions.Bottom.VFX(new TimeWarpTurnEndEffect());
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(player, magicNumber));
        GameActions.Bottom.StackPower(new EnergizedPower(player, secondaryValue));
        GameActions.Bottom.Add(new PressEndTurnButtonAction());
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return player.exhaustPile.size() > player.drawPile.size();
    }
}