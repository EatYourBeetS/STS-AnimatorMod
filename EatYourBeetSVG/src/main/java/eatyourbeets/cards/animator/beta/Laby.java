package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.animator.LabyPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Laby extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Laby.class).SetPower(2, CardRarity.UNCOMMON);

    public Laby()
    {
        super(DATA);

        Initialize(0, 0, 3, 40);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void update()
    {
        super.update();

        if (player != null && player.isDraggingCard && player.hoveredCard == this)
        {
            for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
            {
                if (GameUtilities.IsAttacking(enemy.intent))
                {
                    GR.UI.CombatScreen.AddSubIntent(enemy, EnchantedArmorPower.CalculateDamageReduction(enemy, secondaryValue));
                }
            }
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, secondaryValue));
        GameActions.Bottom.StackPower(new LabyPower(p, 1, upgraded));
    }
}