package eatyourbeets.cards.animatorClassic.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.animatorClassic.LabyPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Laby extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Laby.class).SetSeriesFromClassPackage().SetPower(2, CardRarity.UNCOMMON);

    public Laby()
    {
        super(DATA);

        Initialize(0, 0, 3, 40);


    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddPlayerEnchantedArmor(secondaryValue);
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, secondaryValue));
        GameActions.Bottom.StackPower(new LabyPower(p, 1, upgraded));
    }
}