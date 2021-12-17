package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.pcl.HolyGrailRelic;
import pinacolada.utilities.PCLActions;

public class HolyGrail extends PCLCard_UltraRare implements OnBattleEndSubscriber
{
    public static final PCLCardData DATA = Register(HolyGrail.class)
            .SetSkill(1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Fate);

    public HolyGrail()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, -1);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetInnate(true);
        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLCombatStats.onBattleEnd.Subscribe(this);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        PCLCombatStats.onBattleEnd.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new OfferingEffect(), Settings.FAST_MODE ? 0.1f : 0.5f);
        PCLActions.Bottom.Callback(() ->
        {
            player.decreaseMaxHealth(secondaryValue);
            player.energy.recharge();
        });
        PCLActions.Bottom.Draw(magicNumber);
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