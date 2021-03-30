package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainBlock;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainTempHP;
import eatyourbeets.misc.GenericEffects.GenericEffect_StackPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameUtilities;

public class Greed extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Greed.class).SetPower(2, CardRarity.RARE).SetMaxCopies(1);
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

        SetSynergy(Synergies.FullmetalAlchemist);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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