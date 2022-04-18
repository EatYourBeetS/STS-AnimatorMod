package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.EnchantedArmorPlayerPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Albedo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Albedo.class)
            .SetAttack(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Albedo()
    {
        super(DATA);

        Initialize(16, 0, 1);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Dark(2, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddPlayerEnchantedArmor(magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.StackPower(new EnchantedArmorPlayerPower(p, magicNumber));
        GameActions.Bottom.GainTemporaryArtifact(1);
        GameActions.Bottom.StackPower(new AlbedoPower(p, 1));
    }

    public static class AlbedoPower extends AnimatorPower
    {
        public AlbedoPower(AbstractCreature owner, int amount)
        {
            super(owner, Albedo.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Top.FetchFromPile(name, amount, player.drawPile)
            .SetOptions(false, true)
            .SetFilter(c -> c.type == CardType.POWER);
            RemovePower();
            this.flash();
        }
    }
}