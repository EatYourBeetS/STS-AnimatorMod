package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Hakurou extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Hakurou.class);

    public Hakurou()
    {
        super(ID, 2, CardRarity.COMMON, EYBAttackType.Normal);

        Initialize(1, 0, 3);
        SetUpgrade(0, 0, 1);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new DieDieDieEffect());

        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        }

        if (HasSynergy())
        {
            GameActions.Bottom.GainAgility(1);
        }
    }
}