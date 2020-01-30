package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class SilverFang extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(SilverFang.class);

    public SilverFang()
    {
        super(ID, 2, CardRarity.COMMON, CardType.ATTACK, CardTarget.SELF_AND_ENEMY);

        Initialize(8, 3, 1);
        SetUpgrade(3, 1, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.OnePunchMan);
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