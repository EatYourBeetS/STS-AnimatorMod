package eatyourbeets.cards.animatorClassic.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NononJakuzure extends AnimatorClassicCard implements OnSynergySubscriber
{
    public static final EYBCardData DATA = Register(NononJakuzure.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public NononJakuzure()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetExhaust(true);
        this.series = CardSeries.KillLaKill;
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player.hand.contains(this))
        {
            CombatStats.onSynergy.Subscribe(this);
        }
    }

    @Override
    public void OnSynergy(AbstractCard card)
    {
        if (!player.hand.contains(this))
        {
            CombatStats.onSynergy.Unsubscribe(this);
        }
        else if (card != this)
        {
            GameUtilities.IncreaseSecondaryValue(this, 1, false);
            flash();
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Cycle(name, magicNumber)
        .AddCallback(() ->
        { //
            GameActions.Top.Motivate(1)
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
                GameActions.Bottom.Motivate(remaining);
            }
        }
        else if (remaining > 1)
        {
            GameActions.Top.Motivate(1)
            .MotivateZeroCost(false)
            .AddCallback(remaining - 1, this::OnMotivate);
        }
    }
}