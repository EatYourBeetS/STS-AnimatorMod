package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.NextTurnDexterityPower;
import eatyourbeets.powers.PlayerStatistics;

public class SilverFang extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(SilverFang.class.getSimpleName(), EYBCardBadge.Synergy);

    public SilverFang()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(8, 8, 2);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + MartialArtist.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (HasActiveSynergy())
        {
            GameActionsHelper.GainAgility(1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeBlock(3);
        }
    }
}