package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_FullmetalAlchemist extends Strike
{
    public static final String ID = Register(Strike_FullmetalAlchemist.class).ID;

    public Strike_FullmetalAlchemist()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5, 0, 0);
        SetUpgrade(3, 0);

        SetCooldown(1, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Lightning());
    }
}