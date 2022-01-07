package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.actions.special.SelectCreature;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Megaman extends PCLCard
{
    public static final PCLCardData DATA = Register(Megaman.class).SetAttack(1, CardRarity.RARE, PCLAttackType.Ranged, EYBCardTarget.None)
            .SetMultiformData(3, false).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Megaman);

    public Megaman()
    {
        super(DATA);

        Initialize(2, 0, 3, 22);
        SetUpgrade(0, 0, 1, 6);

        SetAffinity_Silver(1, 0, 3);
        SetAffinity_Light(1);

        SetHitCount(3);
        SetUnique(true, -1);

        SetCooldown(3, 0, __ -> {});
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form]);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (cooldown.GetCurrent() == 0) {
            switch (auxiliaryData.form) {
                case 1:
                    SetAttackType(PCLAttackType.Fire);
                    break;
                case 2:
                    SetAttackType(PCLAttackType.Ice);
                    break;
                default:
                    SetAttackType(PCLAttackType.Electric);
                    break;
            }
            PCLGameUtilities.ModifyDamage(this, baseDamage + secondaryValue, true);
            PCLGameUtilities.ModifyHitCount(this, 1, true);
        }
        else {
            SetAttackType(PCLAttackType.Ranged);
            PCLGameUtilities.ModifyDamage(this, baseDamage, true);
            PCLGameUtilities.ModifyHitCount(this, baseHitCount, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectCreature(SelectCreature.Targeting.Any, this.name).IsCancellable(false).AddCallback((c) -> {
            if (c.isPlayer) {
                cooldown.ProgressCooldown(magicNumber);
            } else {
                PCLActions.Bottom.DealCardDamage(this, c, attackType == PCLAttackType.Ranged ? AttackEffects.GUNSHOT : AttackEffects.PSYCHOKINESIS)
                        .forEach(d -> d.SetVFX(true, false)
                                .SetVFXColor(GetVFXColor(), GetVFXColor())
                                .SetDamageEffect(enemy ->
                                        attackType == PCLAttackType.Ranged ? 0.1f : PCLGameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, GetVFXColor())).duration * 0.1f));
                cooldown.ProgressCooldownAndTrigger(m);
            }
        });

    }

    protected Color GetVFXColor() {
        switch (attackType) {
            case Fire:
                return Color.FIREBRICK;
            case Ice:
                return Color.SKY;
            case Electric:
                return Color.GOLDENROD;
            default:
                return Color.WHITE;
        }
    }
}