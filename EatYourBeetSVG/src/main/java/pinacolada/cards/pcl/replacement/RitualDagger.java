package pinacolada.cards.pcl.replacement;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.common.BlindedPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class RitualDagger extends PCLCard
{
    public static final PCLCardData DATA = Register(RitualDagger.class)
            .SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Normal, EYBCardTarget.Normal)
            .SetSeries(CardSeries.Fate)
            .SetColor(CardColor.COLORLESS);

    public RitualDagger()
    {
        super(DATA);

        Initialize(9, 0, 3);
        SetUpgrade(3, 0, 2);

        SetAffinity_Light(1, 0, 4);
        SetAffinity_Dark(1);
        SetExhaust(true);
        SetUnique(true, -1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            return super.ModifyDamage(enemy, amount * (enemy.hasPower(BlindedPower.POWER_ID) ? 2 : 1));
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.DAGGER)
                .forEach(d -> d.SetSoundPitch(0.5f, 0.7f).SetVFXColor(Color.GOLDENROD)
                        .SetDamageEffect(c -> PCLGameEffects.Queue.Add(VFX.IronWave(player.hb, c.hb).SetColor(Color.GOLDENROD)).duration * 0.5f)
                        .AddCallback(enemy -> {
                            if (!PCLGameUtilities.IsDeadOrEscaped(enemy)) {
                                PCLActions.Bottom.RemovePower(p, m, ArtifactPower.POWER_ID);
                                PCLActions.Bottom.RemovePower(p, m, IntangiblePower.POWER_ID);
                                PCLActions.Bottom.RemovePower(p, m, IntangiblePlayerPower.POWER_ID);
                            }
                            if (PCLGameUtilities.IsFatal(enemy, true) && info.TryActivateLimited()) {
                                PCLActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                                        .IncludeMasterDeck(true)
                                        .IsCancellable(false);
                            }

                        }));
    }
}