package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Curse_GriefSeed extends AnimatorCard_Curse implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(Curse_GriefSeed.class)
            .SetCurse(1, EYBCardTarget.None, false)
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
        CombatStats.onPurge.Subscribe(this);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card != null && this.uuid.equals(card.uuid)) {
            GameActions.Bottom.Callback(() ->
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
                    GameActions.Top.RemovePower(player, powers.Retrieve(rng));
                }
            });
            CombatStats.onPurge.Unsubscribe(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < secondaryValue; i++) {
            GameActions.Bottom.SelectFromHand(name, 1, false)
                    .SetFilter(GameUtilities::HasCooldown)
                    .AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    if (GameUtilities.HasCooldown(c)) {
                        ((EYBCard) c).cooldown.ProgressCooldown(magicNumber);
                        c.flash();
                    }
                }
            });
        }

    }
}
