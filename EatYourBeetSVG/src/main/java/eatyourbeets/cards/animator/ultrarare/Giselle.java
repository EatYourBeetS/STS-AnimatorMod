package eatyourbeets.cards.animator.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.orbs.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class Giselle extends AnimatorCard_UltraRare implements StartupCard
{
    public static final String ID = Register(Giselle.class.getSimpleName(), EYBCardBadge.Special);

    public Giselle()
    {
        super(ID, 2, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(26,0);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        BurningPower burning = GameUtilities.GetPower(m, BurningPower.POWER_ID);
        if (burning != null)
        {
            int amount = burning.amount * (upgraded ? 2 : 1);

            GameActions.Bottom.ApplyBurning(p, m, amount);
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.ChannelOrb(new Fire(), false);

        return true;
    }
}