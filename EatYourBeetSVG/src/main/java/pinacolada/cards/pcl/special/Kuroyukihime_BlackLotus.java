package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.colorless.uncommon.Kuroyukihime;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;

public class Kuroyukihime_BlackLotus extends PCLCard
{
    public static final PCLCardData DATA = Register(Kuroyukihime_BlackLotus.class)
            .SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Ranged, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(Kuroyukihime.DATA.Series);

    public Kuroyukihime_BlackLotus()
    {
        super(DATA);

        Initialize(7, 5, 2, 1);
        SetUpgrade(2, 1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Silver(1);

        SetAffinityRequirement(PCLAffinity.General, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.SFX(SFX.ATTACK_DEFECT_BEAM);
        PCLActions.Bottom.VFX(VFX.SweepingBeam(p.hb, VFX.FlipHorizontally(), new Color(0.24f, 0, 0.4f, 1f)), 0.3f);
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.FIRE);

        PCLActions.Bottom.GainTechnic(secondaryValue);

        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            PCLActions.Bottom.Draw(magicNumber);
            PCLActions.Bottom.ModifyAllInstances(uuid)
            .AddCallback(card ->
            {
                final Kuroyukihime_BlackLotus c = (Kuroyukihime_BlackLotus)card;
                c.SetAffinityRequirement(PCLAffinity.General, c.affinities.GetRequirement(PCLAffinity.General) + 1);
            });
        });
    }
}