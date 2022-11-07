package eatyourbeets.cards.animatorClassic.colorless.uncommon;

import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.animatorClassic.special.Magilou_Bienfu;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnCardResetSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Magilou extends AnimatorClassicCard implements OnCardResetSubscriber
{
    public static final EYBCardData DATA = Register(Magilou.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetMaxCopies(1).SetSeries(CardSeries.TalesOfBerseria);
    static
    {
        DATA.AddPreview(new Magilou_Bienfu(), false);
    }

    public Magilou()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
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
            .AddCallback(() -> GameActions.Top.MakeCardInHand(new Magilou_Bienfu()))
            .SetDuration(0.15f, true);
        }
        else
        {
            LoadImage("2");
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(new TriggerOrbPassiveAbility(magicNumber));
        GameActions.Bottom.Callback(new WaitRealtimeAction(0.3f), () -> GameActions.Bottom.Add(new EvokeOrbAction(1)));
    }
}