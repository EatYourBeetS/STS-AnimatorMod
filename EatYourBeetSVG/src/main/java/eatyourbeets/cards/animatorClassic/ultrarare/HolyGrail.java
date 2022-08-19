package eatyourbeets.cards.animatorClassic.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.animator.HolyGrailRelic;
import eatyourbeets.utilities.GameActions;

public class HolyGrail extends AnimatorClassicCard_UltraRare implements OnBattleEndSubscriber
{
    public static final EYBCardData DATA = Register(HolyGrail.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public HolyGrail()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, -1);

        SetInnate(true);
        SetRetain(true);
        SetExhaust(true);
        SetSeries(CardSeries.Fate);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        CombatStats.onBattleEnd.Subscribe(this);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        CombatStats.onBattleEnd.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new OfferingEffect(), Settings.FAST_MODE ? 0.1f : 0.5f);
        GameActions.Bottom.Callback(() ->
        {
            player.decreaseMaxHealth(secondaryValue);
            player.energy.recharge();
        });
        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    public void OnBattleEnd()
    {
        if (player.hand.contains(this))
        {
            player.increaseMaxHp(2, true);

            AbstractRelic relic = player.getRelic(HolyGrailRelic.ID);
            if (relic != null)
            {
                relic.flash();
            }
        }
    }
}