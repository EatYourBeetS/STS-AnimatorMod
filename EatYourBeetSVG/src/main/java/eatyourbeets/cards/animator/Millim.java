package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Millim extends AnimatorCard
{
    public static final String ID = Register(Millim.class.getSimpleName(), EYBCardBadge.Synergy);

    public Millim()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(6,0, 2);

        SetUnique(true);
        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.ApplyBurning(m, p, magicNumber);
        GameActions.Bottom.ApplyPoison(m, p, magicNumber);

        if (HasActiveSynergy())
        {
            GameActions.Bottom.Draw(2);
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }

    @Override
    public void upgrade()
    {
        this.timesUpgraded += 1;

        upgradeDamage(1);
        upgradeMagicNumber(1);

        //upgradeBlock(1);
        this.upgraded = true;
        this.name = cardData.strings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }
}