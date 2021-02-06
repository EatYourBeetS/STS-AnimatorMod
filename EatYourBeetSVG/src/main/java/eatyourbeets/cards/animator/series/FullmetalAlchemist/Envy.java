package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.animator.EnvyPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Envy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Envy.class).SetPower(2, CardRarity.RARE);

    public Envy()
    {
        super(DATA);

        Initialize(0, 0);

        SetEthereal(true);
        SetSynergy(Synergies.FullmetalAlchemist);
        SetShapeshifter();
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
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifyMagicNumber(this, Math.floorDiv(player.maxHealth - player.currentHealth, 5), true);
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
}