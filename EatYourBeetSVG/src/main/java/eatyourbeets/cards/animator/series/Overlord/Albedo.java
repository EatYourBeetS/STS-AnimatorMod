package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.animator.EnchantedArmorPower;
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
    public void OnDrag(AbstractMonster m)
    {
        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddEnchantedArmor(damage);
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