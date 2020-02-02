package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class RoryMercury extends AnimatorCard
{
    public static final String ID = Register(RoryMercury.class);

    public RoryMercury()
    {
        super(ID, 1, CardRarity.UNCOMMON, EYBAttackType.Normal, CardTarget.ALL);

        Initialize(2, 0, 0);
        SetUpgrade(2, 0, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().SetIconTag("???").AddMultiplier(2);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainForce(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY).AddCallback(this::OnDamageDealt);
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY).AddCallback(this::OnDamageDealt);
    }

    private void OnDamageDealt(AbstractCreature enemy)
    {
        if (enemy != null && !GameUtilities.IsDeadOrEscaped(enemy) && enemy.lastDamageTaken > 0)
        {
            GameActions.Bottom.ApplyVulnerable(player, enemy, 1);
        }
    }
}