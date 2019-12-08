package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Lancer extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Lancer.class.getSimpleName(), EYBCardBadge.Special);

    public Lancer()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(6,0, 1);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        tmp += MartialArtist.GetScaling();

        if (mo != null)
        {
            tmp += (tmp * (1 - GameUtilities.GetHealthPercentage(mo)));
        }

        return super.calculateModifiedCardDamage(player, mo, tmp);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractGameAction.AttackEffect attackEffect;
        if (this.damage >= 15)
        {
            GameActionsHelper.VFX(new ClashEffect(m.hb.cX, m.hb.cY), 0.1F);
            attackEffect = AbstractGameAction.AttackEffect.NONE;
        }
        else
        {
            attackEffect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
        }

        GameActionsHelper.DamageTargetPiercing(p, m, this, attackEffect);
        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, magicNumber, false), magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(2);
        }
    }
}