package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.animator.EnvyPower;
import eatyourbeets.utilities.GameActions;

public class Envy extends AnimatorCard
{
    public static final String ID = Register_Old(Envy.class);

    public Envy()
    {
        super(ID, 2, CardRarity.RARE, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0);

        SetEthereal(true);
        SetSynergy(Synergies.FullmetalAlchemist, true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        if (magicNumber > 0)
        {
            return TempHPAttribute.Instance.SetCard(this, true);
        }

        return null;
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new EnvyPower(p, 1));

        int tempHP = Math.floorDiv(p.maxHealth - p.currentHealth, 5);
        if (tempHP > 0)
        {
            GameActions.Bottom.GainTemporaryHP(tempHP);
        }
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        baseMagicNumber = magicNumber = Math.floorDiv(player.maxHealth - player.currentHealth, 5);
    }
}