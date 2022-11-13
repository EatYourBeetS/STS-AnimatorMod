package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_ApplyToPlayer;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_GainBlock;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_GainTempHP;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameUtilities;

public class Greed extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Greed.class).SetSeriesFromClassPackage().SetPower(2, CardRarity.RARE).SetMaxCopies(1);
    public static final int BLOCK = 7;
    public static final int TEMP_HP = 6;
    public static final int MALLEABLE = 5;
    public static final int PLATED_ARMOR = 4;
    public static final int METALLICIZE = 3;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Greed()
    {
        super(DATA);

        Initialize(0,0, 200, 1);
        SetUpgrade(0,0, -50);

        
    }

    @Override
    public String GetRawDescription()
    {
        return super.GetRawDescription(BLOCK, TEMP_HP, MALLEABLE, PLATED_ARMOR, METALLICIZE);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.IncreaseSecondaryValue(this, Math.floorDiv(player.gold, magicNumber), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_GainBlock(BLOCK));
            choices.AddEffect(new GenericEffect_GainTempHP(TEMP_HP));
            choices.AddEffect(new GenericEffect_ApplyToPlayer(PowerHelper.Malleable, MALLEABLE));
            choices.AddEffect(new GenericEffect_ApplyToPlayer(PowerHelper.PlatedArmor, PLATED_ARMOR));
            choices.AddEffect(new GenericEffect_ApplyToPlayer(PowerHelper.Metallicize, METALLICIZE));
        }

        choices.Select(secondaryValue, m);
    }
}