package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Strike_Kancolle extends Strike
{
    public static final String ID = Register(Strike_Kancolle.class).ID;

    public Strike_Kancolle()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0, 5);
        SetUpgrade(3, 0);

        SetHealing(true);
        SetSynergy(Synergies.Kancolle);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainGold(magicNumber);
        }
    }
}