package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class SilverFang extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(SilverFang.class, EYBCardBadge.Synergy);

    public SilverFang()
    {
        super(ID, 2, CardRarity.COMMON, CardType.ATTACK, CardTarget.SELF_AND_ENEMY);

        Initialize(12, 0, 4);
        SetUpgrade(4, 0, 0);

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
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        AgilityPower.PreserveOnce();

        if (HasSynergy())
        {
            GameActions.Bottom.GainBlock(magicNumber);
        }
    }
}