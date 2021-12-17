package pinacolada.cards.pcl.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class NononJakuzure extends PCLCard implements OnSynergySubscriber
{
    public static final PCLCardData DATA = Register(NononJakuzure.class)
            .SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.KillLaKill);

    public NononJakuzure()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetAffinity_Orange(1);
        
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player.hand.contains(this))
        {
            PCLCombatStats.onSynergy.Subscribe(this);
        }
    }

    @Override
    public void OnSynergy(AbstractCard card)
    {
        if (!player.hand.contains(this))
        {
            PCLCombatStats.onSynergy.Unsubscribe(this);
        }
        else if (card != this)
        {
            PCLGameUtilities.IncreaseSecondaryValue(this, 1, false);
            flash();
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Cycle(name, magicNumber)
        .AddCallback(() ->
        { //
            PCLActions.Top.Motivate()
            .MotivateZeroCost(false)
            .AddCallback(secondaryValue, this::OnMotivate);
        });
    }

    private void OnMotivate(Integer remaining, AbstractCard card)
    {
        if (card == null)
        {
            if (remaining > 0)
            {
                PCLActions.Bottom.Motivate(remaining);
            }
        }
        else if (remaining > 1)
        {
            PCLActions.Top.Motivate()
            .MotivateZeroCost(false)
            .AddCallback(remaining - 1, this::OnMotivate);
        }
    }
}