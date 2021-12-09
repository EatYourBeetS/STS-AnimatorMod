package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.IonizingStorm;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.common.TemporaryMalleablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Mayuri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mayuri.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new IonizingStorm(), false));

    public Mayuri()
    {
        super(DATA);

        Initialize(4, 23, 2, 6);
        SetUpgrade(2, 2, 0);
        SetAffinity_Light(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return other.rarity.equals(CardRarity.BASIC);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.LIGHTNING);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.StackPower(p, new TemporaryMalleablePower(p, secondaryValue));
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Player(), magicNumber).AddCallback(() -> {
            for (EnemyIntent intent : GameUtilities.GetIntents()) {
                if (intent.GetDamage(true) > player.maxHealth / 2 && info.TryActivateLimited()) {
                    AbstractCard c = new IonizingStorm();
                    c.applyPowers();
                    GameActions.Bottom.PlayCopy(c, null);
                    break;
                }
            }
        });
    }
}