package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;

public class Albedo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Albedo.class).SetAttack(2, CardRarity.RARE);

    public Albedo()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(3, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, damage));

        if (HasSynergy())
        {
            GameActions.Bottom.GainTemporaryArtifact(1);
        }
    }
}