package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStanceChangedSubscriber;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.ExitStanceNextTurnPower;
import eatyourbeets.stances.*;
import eatyourbeets.utilities.GameActions;

public class KotarouTennouji extends AnimatorCard implements OnStanceChangedSubscriber
{
    public static final EYBCardData DATA = Register(KotarouTennouji.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Normal).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public KotarouTennouji()
    {
        super(DATA);

        Initialize(8, 0, 0);
        SetUpgrade(3, 0, 0);
        SetAffinity_Star(1, 0, 2);

        SetUnique(true, true);
        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_EnterStance(MightStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(VelocityStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(WisdomStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(EnduranceStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(InvocationStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(DesecrationStance.STANCE_ID));
        }

        choices.Select(1, m);

        GameActions.Bottom.ApplyPower(new ExitStanceNextTurnPower(p, 1));
    }

    @Override
    public void OnStanceChanged(AbstractStance oldStance, AbstractStance newStance)
    {
        if (player.hand.contains(this) && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
            .IncludeMasterDeck(true)
            .IsCancellable(false);
            flash();
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onStanceChanged.Subscribe(this);
    }
}