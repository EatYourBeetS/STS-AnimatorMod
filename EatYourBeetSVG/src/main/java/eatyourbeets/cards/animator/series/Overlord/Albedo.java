package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Albedo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Albedo.class).SetAttack(2, CardRarity.RARE);

    public Albedo()
    {
        super(DATA);

        Initialize(8, 2);
        SetUpgrade(3, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
        {
            if (GameUtilities.IsAttacking(enemy.intent))
            {
                GR.UI.CombatScreen.AddSubIntent(enemy, EnchantedArmorPower.CalculateDamageReduction(enemy, damage));
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(2);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, damage));

        if (HasSynergy())
        {
            GameActions.Bottom.GainTemporaryArtifact(1);
        }
    }
}