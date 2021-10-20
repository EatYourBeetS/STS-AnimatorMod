package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainBlock;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainTempHP;
import eatyourbeets.misc.GenericEffects.GenericEffect_StackPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameUtilities;

public class Greed extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Greed.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int BLOCK = 14;
    public static final int TEMP_HP = 12;
    public static final int MALLEABLE = 10;
    public static final int PLATED_ARMOR = 8;
    public static final int METALLICIZE = 6;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Greed()
    {
        super(DATA);

        Initialize(0,0, 3, 1);
        SetUpgrade(0,0, -1);

        SetAffinity_Steel(2);

        SetRetain(true);
    }

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription(BLOCK, TEMP_HP, MALLEABLE, PLATED_ARMOR, METALLICIZE);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.IncreaseSecondaryValue(this, Math.floorDiv(player.hand.size(), magicNumber), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_GainBlock(BLOCK));
            choices.AddEffect(new GenericEffect_GainTempHP(TEMP_HP));
            choices.AddEffect(new GenericEffect_StackPower(PowerHelper.Malleable, MALLEABLE));
            choices.AddEffect(new GenericEffect_StackPower(PowerHelper.PlatedArmor, PLATED_ARMOR));
            choices.AddEffect(new GenericEffect_StackPower(PowerHelper.Metallicize, METALLICIZE));
        }

        choices.Select(secondaryValue, m);
    }
}