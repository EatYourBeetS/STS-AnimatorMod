package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.NoGameNoLife.Shiro;
import eatyourbeets.cards.animator.special.Excalibur;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.subscribers.OnCostRefreshSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Nyanta extends AnimatorCard implements OnSynergySubscriber {
    public static final EYBCardData DATA = Register(Nyanta.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    public Nyanta() {
        super(DATA);

        Initialize(3, 0, 3);
        SetUpgrade(1,0);
        SetScaling(0,1,0);
        SetRetain(true);

        SetCooldown(7, -1, this::OnCooldownCompleted);

        SetSynergy(Synergies.LogHorizon);
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
            cooldown.ProgressCooldownAndTrigger(1, null);
            flash();
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
            .SetVFX(true, false);
        }

        cooldown.ProgressCooldownAndTrigger(1, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
       GameActions.Bottom.GainAgility(CombatStats.SynergiesThisTurn());
    }
}