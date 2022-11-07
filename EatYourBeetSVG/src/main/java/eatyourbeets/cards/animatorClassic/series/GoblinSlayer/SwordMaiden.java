package eatyourbeets.cards.animatorClassic.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class SwordMaiden extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(SwordMaiden.class).SetSeriesFromClassPackage().SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public SwordMaiden()
    {
        super(DATA);

        Initialize(0, 0, 6);

        SetExhaust(true);

    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainForce(1, true);
        GameActions.Bottom.GainAgility(1, true);
        GameActions.Bottom.GainIntellect(1, true);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.Callback(() ->
        {
            for (int i = player.powers.size() - 1; i >= 0; i--)
            {
                AbstractPower power = player.powers.get(i);
                if (power.type == AbstractPower.PowerType.DEBUFF)
                {
                    GameActions.Bottom.RemovePower(player, player, power);
                    return;
                }
            }
        });
    }
}