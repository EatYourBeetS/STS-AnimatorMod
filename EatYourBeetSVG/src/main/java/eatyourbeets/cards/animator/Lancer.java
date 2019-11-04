package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Lancer extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Lancer.class.getSimpleName(), EYBCardBadge.Special);

    public Lancer()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(6,0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        tmp += MartialArtist.GetScaling();

        if (mo != null && (mo.maxHealth / (float)mo.currentHealth) < 0.5f)
        {
            return super.calculateModifiedCardDamage(player, mo, tmp * 2);
        }
        else
        {
            return super.calculateModifiedCardDamage(player, mo, tmp);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractGameAction.AttackEffect attackEffect;
        if (m.currentHealth <= (m.maxHealth / 2))
        {
            attackEffect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        }
        else
        {
            attackEffect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
        }

        GameActionsHelper.DamageTargetPiercing(p, m, this, attackEffect);

        if (m.currentBlock > 0)
        {
            GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);
            GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 1, false), 1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(3);
        }
    }
}