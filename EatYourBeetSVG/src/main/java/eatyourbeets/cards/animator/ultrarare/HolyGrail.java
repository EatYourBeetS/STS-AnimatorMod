package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.animator.HolyGrailRelic;
import eatyourbeets.utilities.GameActions;

public class HolyGrail extends AnimatorCard_UltraRare implements OnBattleEndSubscriber
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
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PlayerStatistics.onBattleEnd.Subscribe(this);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        PlayerStatistics.onBattleEnd.Subscribe(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new OfferingEffect(), Settings.FAST_MODE ? 0.1f : 0.5f);
        GameActions.Bottom.Callback(() ->
        {
            AbstractDungeon.player.decreaseMaxHealth(secondaryValue);
            AbstractDungeon.player.energy.recharge();
        });
        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    public void OnBattleEnd()
    {
        AbstractPlayer p = AbstractDungeon.player;

        if (p.hand.contains(this))
        {
            p.increaseMaxHp(2, true);

            AbstractRelic relic = p.getRelic(HolyGrailRelic.ID);
            if (relic != null)
            {
                relic.flash();
            }
        }
    }
}