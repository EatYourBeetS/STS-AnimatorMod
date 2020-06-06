package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class NononJakuzure extends AnimatorCard implements OnSynergySubscriber
{
    public static final EYBCardData DATA = Register(NononJakuzure.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public NononJakuzure()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetExhaust(true);
        SetSynergy(Synergies.KillLaKill);
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
    public void OnSynergy(AnimatorCard card)
    {
        if (!player.hand.contains(this))
        {
            CombatStats.onSynergy.Unsubscribe(this);
        }
        else
        {
            baseSecondaryValue = (secondaryValue += 1);

            flash();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Cycle(name, magicNumber)
        .AddCallback(() ->
        { //
            GameActions.Top.Motivate()
            .MotivateZeroCost(false)
            .AddCallback(secondaryValue, this::OnMotivate);
        });
    }

    private void OnMotivate(Integer remaining, AbstractCard card)
    {
        if (remaining > 1)
        {
            if (card != null)
            {
                GameActions.Top.Motivate()
                .MotivateZeroCost(false)
                .AddCallback(remaining - 1, this::OnMotivate);
            }
            else
            {
                GameActions.Bottom.Motivate(remaining);
            }
        }
    }
}