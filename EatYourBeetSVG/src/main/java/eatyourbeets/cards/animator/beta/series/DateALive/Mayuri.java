package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.common.ElectrifiedPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Mayuri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mayuri.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    public Mayuri()
    {
        super(DATA);

        Initialize(4, 23, 3, 15);
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

        GameActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), magicNumber);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Player(), magicNumber).AddCallback(() -> {
            for (EnemyIntent intent : GameUtilities.GetIntents()) {
                if (CheckSpecialCondition(true)) {
                    GameActions.Bottom.Callback(() -> CommonTriggerablePower.AddEffectBonus(ElectrifiedPower.POWER_ID, secondaryValue));
                    break;
                }
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        float estimatedDifference = player.hasPower(VulnerablePower.POWER_ID) ? 1f : 1f + (AnimatorVulnerablePower.ATTACK_MULTIPLIER + AnimatorVulnerablePower.PLAYER_MODIFIER) / 100f;
        for (EnemyIntent intent : GameUtilities.GetIntents()) {
            if (intent.GetDamage(true) * estimatedDifference > player.maxHealth / 4f) {
                return true;
            }
        }
        return false;
    }
}