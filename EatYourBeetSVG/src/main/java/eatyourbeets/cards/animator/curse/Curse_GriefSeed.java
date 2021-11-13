package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Curse_GriefSeed extends AnimatorCard_Curse implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(Curse_GriefSeed.class)
            .SetCurse(1, EYBCardTarget.None, false)
            .SetSeries(CardSeries.MadokaMagica);

    public Curse_GriefSeed()
    {
        super(DATA, false);

        Initialize(0, 0, 2, 2);

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
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card != null && this.uuid.equals(card.uuid)) {
            GameActions.Bottom.WaitRealtime(0.3f);
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
    public void triggerOnEndOfTurnForPlayingCard()
    {
        GameActions.Bottom.Callback(() ->
        {
            GameActions.Bottom.Flash(this);
            GameActions.Bottom.TakeDamage(magicNumber, AttackEffects.FIRE);
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < secondaryValue; i++) {
            GameActions.Bottom.SelectFromHand(name, 1, true).SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).cooldown != null && ((AnimatorCard) c).cooldown.cardConstructor != null).AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    if (c instanceof AnimatorCard) {
                        ((AnimatorCard) c).cooldown.ProgressCooldown(-1);
                        c.flash();
                    }
                }
            });
        }

    }
}
