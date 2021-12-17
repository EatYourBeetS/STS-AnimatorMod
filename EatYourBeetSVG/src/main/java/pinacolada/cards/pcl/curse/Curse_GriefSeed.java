package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Curse_GriefSeed extends PCLCard_Curse implements OnPurgeSubscriber
{
    public static final PCLCardData DATA = Register(Curse_GriefSeed.class)
            .SetCurse(1, eatyourbeets.cards.base.EYBCardTarget.None, false)
            .SetSeries(CardSeries.MadokaMagica);

    public Curse_GriefSeed()
    {
        super(DATA, false);

        Initialize(0, 0, 2, 1);
        SetCostUpgrade(-1);

        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onPurge.Subscribe(this);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue);
        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card != null && this.uuid.equals(card.uuid)) {
            PCLActions.Bottom.Callback(() ->
            {
                RandomizedList<AbstractPower> powers = new RandomizedList<>();
                for (AbstractPower p : player.powers)
                {
                    if (p.type == AbstractPower.PowerType.DEBUFF)
                    {
                        powers.Add(p);
                    }
                }

                if (powers.Size() > 0)
                {
                    PCLActions.Top.RemovePower(player, powers.Retrieve(rng));
                }
            });
            PCLCombatStats.onPurge.Unsubscribe(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < secondaryValue; i++) {
            PCLActions.Bottom.SelectFromHand(name, 1, false)
                    .SetFilter(PCLGameUtilities::HasCooldown)
                    .AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    if (PCLGameUtilities.HasCooldown(c)) {
                        ((PCLCard) c).cooldown.ProgressCooldown(magicNumber);
                        c.flash();
                    }
                }
            });
        }

    }
}
