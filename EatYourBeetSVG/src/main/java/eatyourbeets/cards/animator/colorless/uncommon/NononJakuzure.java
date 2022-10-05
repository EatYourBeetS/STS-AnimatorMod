package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NononJakuzure extends AnimatorCard implements OnAffinitySealedSubscriber
{
    public static final EYBCardData DATA = Register(NononJakuzure.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.KillLaKill);

    public NononJakuzure()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetAffinity_Green(1);
        
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnAffinitySealed(EYBCard card, boolean manual)
   {
        if (player.hand.contains(this) && player.hand.contains(card) && card != this)
        {
            GameUtilities.IncreaseSecondaryValue(this, 1, false);
            GameUtilities.Flash(this, false);
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

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAffinitySealed.Subscribe(this);
    }
}