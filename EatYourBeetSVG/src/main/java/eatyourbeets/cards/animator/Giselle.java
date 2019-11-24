package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.orbs.Fire;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
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
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        BurningPower burning = GameUtilities.GetPower(m, BurningPower.POWER_ID);
        if (burning != null)
        {
            int amount = burning.amount * (upgraded ? 2 : 1);

            GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, amount), amount);
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
        GameActionsHelper.ChannelOrb(new Fire(), false);

        return true;
    }
}