package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.animator.special.Bienfu;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnCardResetSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Magilou extends AnimatorCard implements OnCardResetSubscriber
{
    public static final EYBCardData DATA = Register(Magilou.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
    static
    {
        DATA.AddPreview(new Bienfu(), false);
    }

    public Magilou()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.TalesOfBerseria);
        SetSpellcaster();
    }

    @Override
    public void OnCardReset(AbstractCard card)
    {
        if (card == this)
        {
            LoadImage(null);
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Top.Discard(this, player.hand).ShowEffect(true, true)
            .AddCallback(() -> GameActions.Top.MakeCardInHand(new Bienfu()))
            .SetDuration(0.15f, true);
        }
        else
        {
            LoadImage("2");
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Callback(new TriggerOrbPassiveAbility(magicNumber));
        GameActions.Bottom.Callback(new WaitRealtimeAction(0.3f), () -> GameActions.Bottom.Add(new EvokeOrbAction(1)));
    }
}