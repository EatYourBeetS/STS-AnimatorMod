package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class RoryMercury extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoryMercury.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public RoryMercury()
    {
        super(DATA);

        Initialize(4, 0, 0);
        SetUpgrade(2, 0, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY).AddCallback(this::OnEnemyHit);
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY).AddCallback(this::OnEnemyHit);

        GameActions.Bottom.ModifyAllInstances(uuid)
        .AddCallback(c ->
        {
            if (!c.hasTag(HASTE))
            {
                c.tags.add(HASTE);
            }
        });
    }

    private void OnEnemyHit(AbstractCreature c)
    {
        if (c.hasPower(VulnerablePower.POWER_ID) && CombatStats.TryActivateSemiLimited(cardID))
        {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
            }

            choices.Select(GameActions.Top, 1, (AbstractMonster)c)
            .CancellableFromPlayer(true);
        }
    }
}